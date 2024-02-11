import { createStore } from 'vuex'

// Create a new store instance.
const store = createStore({
  state () {
    return {
      account: {
        id: 0,
        userId: '',
      },

    }
  },
  mutations: {
    setAccount(state, payload){
       // payload가 객체로 전달되기 때문에 객체 디스트럭처링을 사용하여 각각의 프로퍼티에 할당
       state.account = { ...state.account, ...payload };
    },
    // setToken(state, token){
    //   state.account.token = token;
    // }
  }
})

export default store;