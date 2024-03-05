
import axios from 'axios'
//import store from '@/script/store'                    
const getUserInfo = (userId, userPw) => {
    const reqData = {
      'userId': userId,
      'userPw': userPw
    }
  
    //let serverUrl = '//localhost:8081'
  
    return axios.post('/api/all/login', reqData, {
      headers: {
        'Content-type': 'application/json'
      }
    })
  }
  
  export default {
    async doLogin(userId, userPw) {
        try {
            const getUserInfoPromise = getUserInfo(userId, userPw)
            const [userInfoResponse] = await Promise.all([getUserInfoPromise])
            if (userInfoResponse.data.length === 0) {
             return 'notFound'
            } else {
                //console.log("userInfoResponse.data : " + JSON.stringify(userInfoResponse.data));
                localStorage.setItem('user_access_token', userInfoResponse.data.user_access_token)
                localStorage.setItem('user_refresh_token', userInfoResponse.data.user_refresh_token)
                localStorage.setItem('user_role', userInfoResponse.data.user_role)
                localStorage.setItem('user_id', userInfoResponse.data.user_id)
                localStorage.setItem('user_no', userInfoResponse.data.user_no)
                return userInfoResponse
            }
        } catch (err) {
            console.error(err)
        }
    }
  }