<template>
  <div class="container">
    <div class="row row-cols-1 g-3 con_wrap">
      <div class="introduce_container" v-if="currentPage === 'introduce'">
        <img src="/img/img6.png" class="intro-image" >
        <div class="introduce_message">소개가 작성되지 않았습니다.</div>
        <div class="btn-container">
          <button @click="movePage('add_introduce')" class="btn_add_introduction">소개글 작성하기</button>
        </div>
      </div>

      <div class="introduce_div" v-else-if="currentPage === 'add_introduce'">
        <div v-if="buttonType == 0">
          <div class="btn_introduce">
            <button class="btn_introduce_save" 
                    v-if="this.$store.state.account.userRole === 'ROLE_ADMIN'" 
                    @click="btnType">수정하기</button>
            <div class="added_introduce">{{this.intro}}</div>
          </div>
        </div>

        <div v-else>
          <button class="btn_introduce_save" @click="updateIntro">저장하기</button>
          <div class="introduce_textera">
            <textarea v-model="intro" class="textera" placeholder="당신을 소개해주세요."></textarea>
          </div>
        </div>  
      </div>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      requestBody: {},
      currentPage: '',
      userNo: 0,
      intro: '',
      buttonType : 0
    };
  },

  mounted() {
      if(this.userNo !== 'undefined'){
        this.fnGetIntro()   // 회원 소개글 가져오기
      }
    },
    methods: {
      movePage(id) {
        this.currentPage = id;
      },

      fnGetIntro(){
        this.$axios.get('/api/all/getIntro/' + 24)
        .then((res) => {

          this.intro = res.data; 
          this.userNo = 24;

          if(this.intro == null || this.intro == ''){
            this.movePage('introduce');
            this.buttonType = 1;    //저장하기 버튼
          }else{
            //console.log('getIntro : ' + this.intro);
            this.movePage('add_introduce');
          }
        })
        .catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      },

      updateIntro(){    // 소개글 수정
        const data = {
          "introduction"  : this.intro,
          "userNo"        : this.userNo
        }
        
        this.$axios.post( '/api/admin/updateIntro', data, {
          headers: {
            'Content-Type' : 'application/json',
          },
                
        }).then((res) => {
          this.intro = res.data
        }).catch((err) => {
          console.log(err)
        })
        this.btnType();
        this.fnGetIntro();
      },

      btnType(){
        if(this.buttonType == 0 ){
          this.buttonType = 1;    // 저장하기
        }else{
          this.buttonType = 0;    // 수정하기
        }
        
      }
    }
};
</script>


<style scoped>
.container {
    max-width: 1080px;
    margin: 0 auto;
    text-align: center; 
}

.image-container {
    margin-bottom: 20px; 
}

.intro-image {
    width: 100%; 
    max-width: 300px; 
    height: auto;
}

.introduce_message {
    margin-top: 3rem;
    font-size: 2.5rem;
    color: #555;
    margin-bottom: 20px; 
}

.btn-container {
    display: flex;
    justify-content: center;
}

.btn_add_introduction {
    padding: 10px 20px;
    background-color: #007bff;
    color: white;
    border: none;
    font-size: 1.5rem;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
    width: 250px;
}

.btn_add_introduction:hover {
    background-color: #0056b3;
}

.add_introduce {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
}

.btn_introduce {
        margin-bottom: 20px;
        margin-left: auto; /* 우측으로 이동 */
    }

.btn_introduce_save {
        padding: 10px 20px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s;
    }

    .added_introduce {
    text-align: left; /* 좌측 정렬 */
}

.introduce_div{
  width : 70%;
  margin-left: auto;
}

.introduce_textera {
        width: 100%;
    }

.textera {
        background-color: rgba(var(--bs-tertiary-bg-rgb),var(--bs-bg-opacity))!important;
        border: none;
        outline: none;
        width: 100%;
        padding: 10px;
        resize: none; 
    }
</style>