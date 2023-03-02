import { reactive } from 'vue'

export const store = reactive({
  user: {
      
  },

  carts: [],

  baseUrl: "http://localhost:8000",

  setUsername: function(username){
    this.user.name = username;
  },
  
  setUser: function(u){
    this.user = u;
  },

  setCarts: function(cs){
    this.carts = cs;
  },
})