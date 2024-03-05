import * as types from './mutation_types'

export default {
    setAccount(state, payload){
        // payload가 객체로 전달되기 때문에 객체 디스트럭처링을 사용하여 각각의 프로퍼티에 할당
        state.account = { ...state.account, ...payload };
    },
    [types.USER_ID] (state, userId) {
    state.userId = userId
    },
    [types.ERROR_STATE] (state, errorState) {
        state.errorState = errorState
    },
    [types.IS_AUTH] (state, isAuth) {
        state.isAuth = isAuth
    },
    clearAccount(state) {
        state.account = {
            isLogin: false,
            userId: '',
            userRole: '',
            userAccessToken: '',
            userRefreshToken: ''
        };
    }
}