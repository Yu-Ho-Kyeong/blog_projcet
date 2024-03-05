import { createStore } from 'vuex'
import mutation from './mutation';
import actions from "./actions";
import getters from './getters';
// Create a new store instance.
const store = createStore({
  state () {
    return {
      account: {
        id: '',
        user: null,
        token : '',
        isLogin: false,
        
      },
    } 
  },
  actions: actions,
  mutations: mutation,
  getters: getters
})

export default store;