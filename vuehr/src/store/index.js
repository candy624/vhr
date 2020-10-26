import Vue from "vue";
import Vuex from "vuex";
import sockJS from "../utils/sockjs.js";
import stomp from "../utils/stomp.js";

Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    routes: [],
    sessions: {},
    hrs: [],
    currentSession: null,
    currentHr: JSON.parse(window.sessionStorage.getItem("user")),
    filterKey: '',
    stomp: null,
    isDot: {}
  },
  mutations: {
    INIT_CURRENTHR(state, hr) {
      state.currentHr = hr
    },
    initRoutes(state, data) {
      state.routes = data
    }, 
    changeCurrentSession(state, currentSession) {
      Vue.set(state.isDot, state.currentHr.username + "#" + currentSession.username, false)
      state.currentSession = currentSession
    },
    addMessage(state, msg) {
      let mss = state.sessions[state.currentHr.username + "#" + msg.to]
      if(!mss) {
        Vue.set(state.sessions, state.currentHr.username + "#" + msg.to, [])
      }
      state.sessions[state.currentHr.username + "#" + msg.to].push({
        content: msg.content,
        date: new Date(),
        self: !msg.notSelf
      })
    },
    INIT_DATA(state) {
      let date = localStorage.getItem("vue-chat-session")
      if(data) {
        state.sessions = JSON.parse(data)
      }
    },
    INIT_HR(state, data) {
      state.hrs = data
    }
  },
  actions: {
    content(content) {
      content.state.stomp = Stomp.over(new SockJS('/ws/ep'))
      content.state.stomp.content({}, success => {
        content.state.stomp.subscribe('/user/queue/chat', msg => {
          let receiveMsg = JSON.parse(msg.body)
          if(!content.state.currentSession || receiveMsg.from != content.state.currentSession.username) {
            Notification.info({
              title: '[' + receiveMsg + ']发来一条消息',
              message: receiveMsg.content.length > 10 ? receiveMsg.content.substr(0, 10) : receiveMsg.content,
              position: 'bottom-right'
            })
            Vue.set(content.state.isDot, content.state.currentHr.username + "#" + receiveMsg.from, true)
          }
          receiveMsg.notSelf = true
          receiveMsg.to = receiveMsg.from
          content.commit('addMessage', receiveMsg)
        })
      }, error => {

      })
    }, 
    initData(context) {
      content.commit('INIT_DATA')
      getRequest('/chat/hrs').then(resp => {
        if(resp) {
          content.commit('INIT_HR', resp)
        }
      })
    }
  },
  modules: {},
});

store.watch((state) => {
  return state.sessions
}, (val) => {
  localStorage.setItem("vue-chat-session", JSON.stringify(val))
}, {
  deep: true
}) 


export default store
