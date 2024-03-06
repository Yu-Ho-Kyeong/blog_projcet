<template>
  <div class="home">
    <section class="py-1 text-center container">
      <div class="row py-lg-5">
        <div class="col-lg-6 col-md-8 mx-auto">
          <a class="profile-pic" href="" v-if="this.input.imgPath !== ''">
            <img :src="this.input.imgPath" alt="profile">
          </a>

          <a class="profile-pic" href="" v-else>
            <img :src="'/img/profile/defaultProfile.png'" alt="profile">
          </a>
          
          <h1 class="blogName">{{input.name}}</h1>
          <p class="lead text-body-secondary">{{input.introduction}}</p>
          <div></div>
          <p> 
            <a href="#" @click="movePage('board-list')" class="btn btn-primary my-2">게시글</a>
            <a href="#" class="btn btn-secondary my-2">시리즈</a>
            <a href="#" @click="movePage('introduce')" class="btn btn-secondary my-2">소개</a>
          </p>
        </div>

        <div class="home-tab">
          <div class="" v-if="this.currentPage === 'board-list'">
            <BoardList :boardListData="state.boardListData" ref="boardListRef" />
          </div>
          <div class="" v-else-if="this.currentPage === 'introduce'">
            <Introduce />
          </div>
        </div>
      </div>
    </section>

    <!--태그 컴포넌트 -->
    <aside class="main-tag" v-if="this.currentPage === 'board-list' && state.tagItems">     
      <div class="main-tag-name">태그 목록</div>
      <ul class="for_tag">
        <TagList v-for="(titem, idx) in state.tagItems" :key="idx" :titem="titem" 
                @click="fnGetListByTag(`${titem[0]}`)" />
      </ul>
    </aside>
  </div>
</template>

<script>
import BoardList from '@/pages/homeTab/BoardList.vue';
import Introduce from '@/pages/homeTab/Introducing.vue';
import TagList from '@/pages/tag/TagList.vue';

export default {
  components: { BoardList, Introduce, TagList },
  data() {
    return {
      state: {
        tagItems: [],
        boardListData: {}, 
      },
      //userNo : localStorage.getItem('user_no'),
      input:{
        introduction: '',
        blogName: '',
        imgPath: '',
        name: ''
      },
      currentPage: 'board-list',
      allCnt : 0
    }
  },
  mounted () {
    this.fetchTags();
    this.fnAllTagCnt();
    this.fnGetUserInfo();
  },
  methods: {
    initializeObject() {
      this.state.boardListData = {};
      console.log('초기화 완료');
    },

    movePage(id) {
      //console.log('id : ' + id);
      if(id === 'board-list'){
        if(this.currentPage ==='board-list'){
          this.initializeObject();
          this.$refs.boardListRef.fetchBoardItems(this.state.boardListData);
        }else{
          this.currentPage = 'board-list';
        }
      }else if(id === 'introduce'){
        this.currentPage = 'introduce';
      }
    },

    fetchTags() {
      this.$axios.get("/api/tag/all/getTag")
        .then((res) => {  
          this.state.tagItems = res.data;
          //console.log("tag_data : " + JSON.stringify(res.data), null, 2);
        })
        .catch((error) => {
          console.error('Tag API 호출 중 에러 발생:', error);
        });
    },

    // tagName으로 boardList 조회
    fnGetListByTag(tagName) {
      this.state.boardListData.tagName = tagName;
      this.$refs.boardListRef.state.boardByTagItems = [];
      this.$refs.boardListRef.fetchBoardItems(this.state.boardListData);
    },

    fnAllTagCnt() {
      this.$axios.get("/api/tag/all/allTagCnt")
        .then((res) => {  
          this.allCnt = res.data;
          //console.log("tag_data : " + JSON.stringify(res.data), null, 2);
        })
        .catch((error) => {
          console.error('Tag API 호출 중 에러 발생:', error);
        });
    },
    // 회원정보 불러오기
    fnGetUserInfo(){
      const apiUrl = '/api/all/getAdminSetInfo/' + 24
      this.$axios.get(apiUrl)
      .then((res)=>{
        this.input.name = res.data.name;
        this.input.introduction = res.data.introduction;
        this.input.blogName = res.data.blogName;
        this.input.imgPath = res.data.imgPath; 
        //console.log('res : ' + JSON.stringify(res));
      }).catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      });
    },
  }
}
</script>

<style scoped>
  
  .profile-pic{
    flex-direction: column;
    display: inline-flex;
  }
  .profile-pic img{
    width: 100px;
    height: 100px;
    border-radius: 50%;
    object-fit: cover;
    margin-bottom: 10px;
  }
  .container{ 
    max-width: 1080px; 
    position: relative;
  }
  .home-tab{
    text-align: left !important;
    width: 75%;
    margin-left: 160px;
  }
  .for_tag{
    padding-left:0;
  }

  .main-tag{
    position: fixed;
    top: 390px;
    left: calc(50% - 570px); 
    width: 200px;
    background: #fff;
    z-index: 999;
  }

  .main-tag-name{
    font-weight: bold;
    border-bottom: 1px solid rgb(185, 184, 184);
    margin-bottom: 10px;
    padding-bottom: 15px;
  }
  .blogName{
    font-weight: bold;
  }

</style>

