<template>
  <div id="app">
    <div class="sidebar">
      <card></card>
      <list></list>
    </div>
    <div class="main">
      <message></message>
      <usertext></usertext>
    </div>
  </div>
</template>

<script>
import card from "../../components/caht/card.vue";
import list from "../../components/caht/list.vue";
import message from "../../components/caht/message.vue";
import usertext from "../../components/caht/usertext.vue";
export default {
  name: "FriendChat",
  data() {
    return {};
  },
  mounted() {
    this.$store.dispatch("ininData");
  },
  components: {
    card,
    list,
    message,
    usertext
  }
};
</script>

<style  scoped>
#app {
  margin: 20px auto;
  width: 800px;
  height: 600px;
  overflow: hidden;
  border-radius: 10px;
}
.sidebar,
.main {
  height: 100%;
}
.sidebar {
  float: left;
  color: #f4f4f4;
  background-color: #2e3238;
  width: 200px;
}
.main {
  position: relative;
  overflow: hidden;
  background-color: #eee;
}
</style>