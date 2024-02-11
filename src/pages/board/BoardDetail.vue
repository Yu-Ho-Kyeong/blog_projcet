<template>
  <div class="board-detail">
    <div class="board-contents">
      <h1>{{ boardTitle }}</h1>
      <div class="board-information">
        <span class="userId"><strong>{{ boardUserId }}</strong></span>
        <span class="regDate">{{formatRegDate(regDate)}}</span>
      </div>
      <div class="common-buttons">
        <button type="button" class="" v-on:click="fnUpdate">수정</button>
        <button type="button" class="" v-on:click="fnDelete">삭제</button>
        <button type="button" class="" v-on:click="fnList">목록</button>
      </div>
      <div class="tag-name">
        <strong><span class="tagName">{{tag}}</span></strong>
      </div>
    </div>


    <div class="board-series" style="display:none">
      <h2><a href="/">시리즈 제목</a></h2> <!-- 시리즈 제목 -->
      <ol class="board-series-ol">
        <li><a href="/" class="active"></a></li> <!-- 시리즈 리스트 : 현재 클릭된 게시글은 색깔 진하게-->
      </ol>
      <div class="">
        <div class="">
          <svg stroke="currentColor" fill="currentColor" stroke-width="0" viewBox="0 0 24 24" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
            <path d="M7 14l5-5 5 5z"></path>
          </svg>숨기기 <!-- board-series-ol 접기 펼치기  -->
        </div>
        <div class="">
          <div class="series-number">3/6</div> <!-- 페이지 넘버 -->
          <div class="">
            <button class="series-previous"> <!-- 이전 게시물로 이동 -->
              이전
            </button>
            <button class="series-next"> <!-- 다음 게시물로 이동 -->
              다음
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="board-img">
      <img :src="this.imgPath" alt="썸네일">
    </div>

    <div class="board-contents">
      <!-- <span>{{ boardContent }}</span> -->
      <span v-html="boardContent"></span>
    </div>

    <div class="user-profile">
      <div class="left">
        <a class="profile-pic" href="">
          <img alt="profile" fetchpriority="high" width="128" height="128" 
              decoding="async" data-nimg="1" style="color:transparent" 
              src="https://images.velog.io/images/cindy-choi/profile/1dcf051d-4820-472b-a3fc-2782449f2430/9D473C99-114F-4D41-A8F3-46B421A49FD1.png">
        </a>
        <div class="user-profile-detail">
          <a href="">{{ boardUserId }}</a>
          <div>소개글</div>
        </div>
      </div>
    </div>

    <div class="board-move">
      <div class="previous-move">
        <a class="pre-move-link" href="">
          <div class="pre-arrow">
            <!-- 화살표 -->
            <svg stroke="currentColor" fill="currentColor" stroke-width="0" viewBox="0 0 24 24" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
              <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"></path>
            </svg>
          </div>
          <div class="pre-board">
            <div class="description pre">이전 포스트</div>
            <h3><strong>이전글 제목</strong></h3>
          </div>
        </a>
      </div>

      <div class="next-move">
        <a class="next-move-link" href="">
          <div class="next-arrow">
            <!-- 화살표 -->
            <svg stroke="currentColor" fill="currentColor" stroke-width="0" viewBox="0 0 24 24" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z"></path>
            </svg>
          </div>
          <div class="next-board">
            <div class="description next">다음 포스트</div>
            <h3><strong>다음글 제목</strong></h3>
          </div>
        </a>
      </div>
    </div>
    <Comment />
  </div>
</template>

<script>
import { reactive } from 'vue'; 
import Comment from '@/pages/comment/Comment.vue';
export default { 
  components: { Comment },
  
   setup(){
      const state = reactive({
      items: []
      });
     
      return { state };
    },
  data() { //변수생성
    return {
      requestBody: this.$route.query,
      boardNo: this.$route.query.boardNo,
      boardTitle: '',
      boardUserId: '',
      boardContent: '',
      imgPath: '',
      updDate: '',
      regDate: '',
      tag: ''
    }
  },
  mounted() {
    if(this.boardNo != 'undefined'){
      console.log('boardNo : ' + this.boardNo);
      console.log('url : ' + this.$boardUrl);
      
      this.fnGetView()
    }
    
  },
  methods: {
    fnGetView() {
      console.log("fnGetView 작동");
      //console.log("boardNo : " + this.boardNo);
      //console.log("requestBody : " + this.requestBody);
      this.$axios
        .get(this.$boardUrl + "/" +this.boardNo, {
        params: this.requestBody,
      })
      .then((res) => {
        this.boardTitle = res.data.boardTitle
        this.boardUserId = res.data.boardUserId
        this.boardContent = res.data.boardContent
        this.imgPath = res.data.imgPath
        this.updDate = res.data.updDate
        this.regDate = res.data.regDate
        this.tag = res.data.tag
        console.log('data : ' + JSON.stringify(res, null, 2));
      })
      .catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
      
    },
    fnList() {
      delete this.requestBody.boardNo
      this.$router.push({
        path: '/',
        query: this.requestBody
      })
    },
    fnUpdate() {
      // this.requestBody.tag = this.tag
      // this.requestBody.boardContent = this.boardContent
      console.log('requestBody : ' + JSON.stringify(this.requestBody, null, 2));
      this.$router.push({
        path: './write',
        query: this.requestBody
      })
    },
    fnDelete() {
      if (!confirm("삭제하시겠습니까?")) return
      console.log('url : ' + this.$boardUrl + "/" + this.boardNo);
      this.$axios.delete(this.$boardUrl + "/" + this.boardNo)
          .then(() => {
            alert('삭제되었습니다.')
            this.fnList();
          }).catch((err) => {
        console.log(err);
      })
    },
    // y-m-d format date
    formatRegDate(date) {
      console.log('date : ' + date);
      const formattedDate = date.split('T')[0];
      console.log('formattedDate : ' + formattedDate);
      //const formattedDate = new Date(date).toISOString().split('T')[0];
      return formattedDate;
    },
  }
}
</script>

<style scoped>
.board-detail {
  max-width: 800px;
  margin: auto;
  padding: 20px;
  background-color: #fff;
  
  border-radius: 8px;
}

.board-contents {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
}

.board-img{
  margin-top: 2rem;
  margin-bottom: 2rem;
}

h1 {
  color: #333;
  margin-top: 80px;
}

.board-information {
  margin-top: 10px;
  display: flex;
  justify-content: flex-start;
}

.userId {
  margin-right: 10px; /* Optional: Add margin between userId and regdate */
}

.common-buttons {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.board-series {
  display: none;
  margin-top: 20px;
}

.board-series h2 a {
  color: #007bff;
  text-decoration: none;
}

.board-series-ol {
  list-style: none;
  padding: 0;
}

.board-series-ol li {
  display: inline;
  margin-right: 10px;
}

.series-number {
  font-size: 1.2em;
  font-weight: bold;
  margin-right: 10px;
}

.common-buttons button {
  margin-right: 10px;
  border : none;
  background-color: #fff;
}
.user-profile {
  margin-top: 16rem;
  margin-bottom: 6rem;
}
.user-profile .left{
  display: flex;
}
.user-profile-detail{
  display: flex;
  flex-direction: column;
  -webkit-box-pack: center;
  justify-content: center;
  margin-left: 1rem;
  margin-right: 1rem;
}

.board-move {
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* background-color: #f8f8f8; */
  padding: 10px;
  border-radius: 8px;
  margin-top: 20px; /* 조절 가능한 여백 추가 */
}

.previous-move, .next-move {
  display: flex;
  gap: 10px;
  /* background-color: #777; */
  /* color: #777; */
  background-color: #f8f8f8;
}

.pre-move-link, .next-move-link {
  text-decoration: none;
  color: #007bff;
  display: flex;
  align-items: center;
  min-width: 300px;
}

.pre-arrow {
  width: 32px;
  height: 32px;
  border-radius: 16px;
  display: flex;
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  border: 1px solid var(--primary2);
  font-size: 1.5rem;
  color: var(--primary2);
  margin-right: 1rem 
}

.next-arrow {
  width: 32px;
  height: 32px;
  border-radius: 16px;
  display: flex;
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  border: 1px solid var(--primary2);
  font-size: 1.5rem;
  color: var(--primary2);
  margin-right: 1rem 
}

.pre-board, .next-board {
  display: flex;
  flex-direction: column;
}

.profile-pic img{
  width: 120px;
  height: 120px;
  border-radius: 70%; /* 이미지를 원형으로 만듭니다. */
  object-fit: cover;
  margin-right: 10px;
}

.description {
  font-size: 0.8em;
  margin-top:5px;
  margin-bottom:5px;
}

h3 {

  color: #333;
  font-size: 1.2em;
  margin-top:5px;
  margin-bottom:15px;
}

.profile{
  margin-bottom: 25px;
  display: flex;
  align-items: center; /* 이미지와 .comment-info를 수평으로 정렬 */
}

.sc-egiyK img{
  width: 50px;  /* 이미지의 가로 크기 조절 */
  height: 50px; /* 이미지의 세로 크기 조절 */
  border-radius: 70%;
  object-fit: cover;
  margin-right: 10px; /* 이미지 우측에 마진을 추가하여 .comment-info와 간격 확보 */
    
}
.comment {
  display: flex;
  flex-direction: column; /* 컬럼 레이아웃으로 변경 */
  margin-top: 20px;
}

.sc-eGPXGI {
  margin-right: 10px;
}

.profile img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.sc-jIkXHa {
  margin-top: 20px;
}

h4 {
  font-size: 1.2em;
  margin-bottom: 10px;
  color: #333;
}

.sc-evcjhq {
  margin-top: 20px;
}

.sc-fHeRUh {
  display: flex;
  flex-direction: column;
}

.sc-dtDOqo {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.buttons-wrapper {
  display: flex;
  justify-content: flex-end;
}

.sc-jrQzAO {
  background-color: #007bff;
  color: #fff;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.sc-jHkVzv {
  margin-top: 20px;
  margin-bottom: 20px;
  /* border-bottom: 1px solid #ccc; */
}

.sc-dVNjXY {
  padding-top: 10px;
  margin-top: 10px;
}

.comment {
  display: flex;
  margin-top: 20px;
}

.sc-eGPXGI {
  margin-right: 10px;
}

.profile img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-info {
  display: flex;
  flex-direction: column;
  flex: 1; /* .comment-info가 차지할 수 있는 최대 너비를 설정 */
}

.username {
  font-weight: bold;
}

.date {
  color: #555;
  margin-top: 5px; /* 날짜와 유저명 간격 조절 */
}

.sc-eGRUor {
  margin-top: auto; /* .profile의 하단으로 이동 */
  flex: 1;
}


.sc-kYHfwS {
  display: flex;
  align-items: center;
  margin-top: 10px;
  color: #555;
}

.sc-xiLah {
  display: flex;
  align-items: center;
  margin-right: 10px;
}

.sc-xiLah svg {
  margin-right: 5px;
}
</style>