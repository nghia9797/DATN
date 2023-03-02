// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import ElementUI from 'element-ui';
import locale from 'element-ui/lib/locale/lang/vi'
import 'element-ui/lib/theme-chalk/index.css';
import App from './App'
import router from './router'
import { Message } from 'element-ui';
import { MessageBox } from 'element-ui';

Vue.config.productionTip = false;
Vue.use(ElementUI,{ locale });
Vue.prototype.$message = Message;
Vue.prototype.$confirm = MessageBox;
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  data:{
    id: 1,
    name: ""
  }
})
