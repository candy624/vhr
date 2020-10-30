import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(element)
Vue.prototype.$alert = element.MessageBox.alert
Vue.prototype.$confirm = element.MessageBox.confirm

import {
  postRequest,
  postKeyValueRequest,
  putRequest,
  deleteRequest,
  getRequest,
} from '@utils/api'
import {initMenu} from "./utils/menus"


Vue.prototype.postRequest = postRequest;
Vue.prototype.postKeyValueRequest = postKeyValueRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.getRequest = getRequest;

Vue.config.productionTip = false

router.beforeEach((to, from, next) => {
  if (to.path == '/') {
    next()
  } else {
    if (window.sessionStorage.getItem('user')) {
      initMenu(router, store)
      next()
    } else {
      next('/?redirect=' + to.path)
    }
  }
})

new Vue({
  router,
  store,
  render: (h) => h(App)
}).$mount('#app')
