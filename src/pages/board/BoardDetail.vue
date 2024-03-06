<template>
  <div class="board-detail">
    <div class="board-contents">
      <h1>{{ state.boardItems.boardTitle }}</h1>
      <div class="board-information">
        <span class="userId">{{ state.boardItems.name }}</span>
        <span class="seperator1">·</span>
        <span class="regDate">{{formatRegDate(state.boardItems.regDate)}}</span>
      </div>
      <div class="common-buttons" v-if="this.$store.state.account.userRole === 'ROLE_ADMIN'">
        <button type="button" v-if="state.boardItems.boardUserId === this.$store.state.account.userId"
                v-on:click="fnUpdate">수정</button>
        <button type="button"  v-if="state.boardItems.boardUserId === this.$store.state.account.userId"
                v-on:click="fnDelete">삭제</button>
        <button type="button" 
                v-on:click="fnList">목록</button>
      </div>
      <div class="tag-wrap">
        <div class="tag-name" v-for="tag in state.boardItems.tags" :key="tag.tagNo">
          <span class="tagName">{{tag.tagName}}</span>
        </div>
      </div>
      
    </div>

    <div class="board-contents">
      <span v-html="state.boardItems.boardContent"></span>
    </div>

    <div class="user-profile">
      <div class="left">
        <a class="profile-pic" href="">
          <img :src="this.state.boardItems.userProfile" alt="profile">
        </a>
        <div class="user-profile-detail">
          <a class="profile-userId" href="/">{{ state.boardItems.name }}</a>
          <div class="intro">{{state.boardItems.intro}}</div>
        </div>
      </div>
    </div>

    <div class="board-move">
      <div class="previous-move" v-if="minYn=='N'">
        <a class="pre-move-link" @click="fnView(`${state.preBoardItems.boardNo}`)">
          <div class="pre-arrow">
            <!-- 화살표 이미지 -->
            <svg stroke="currentColor" fill="currentColor" stroke-width="0" viewBox="0 0 24 24" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
              <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"></path>
            </svg>
          </div>
          <div class="pre-board">
            <div class="description pre">이전 포스트</div>
            <h3>{{state.preBoardItems.boardNo}}</h3>
            <h3>{{state.preBoardItems.boardTitle}}</h3>
            
          </div>
        </a>
      </div>

      <div class="next-move" v-if="maxYn=='N'">
        <a class="next-move-link" @click="fnView(`${state.nextBoardItems.boardNo}`)">
          <div class="next-arrow">
            <!-- 화살표 이미지 추후 수정예정 -->
            <svg stroke="currentColor" fill="currentColor" stroke-width="0" viewBox="0 0 24 24" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z"></path>
            </svg>
          </div>
          <div class="next-board">
            <div class="description next">다음 포스트</div>
            <h3>{{state.nextBoardItems.boardNo}}</h3>
            <h3>{{state.nextBoardItems.boardTitle}}</h3>
          </div>
        </a>
      </div>
    </div>
    <Comment :parentBoardNo="this.boardNo" ref="commentListRef"/>
  </div>
</template>

<script>
import Comment from '@/pages/comment/Comment.vue';
export default { 
  components: { Comment },
  data() { 
    return {
      state:{
        boardItems : [],      // 현재 게시글 정보
        preBoardItems : [],   // 이전 게시글 정보
        nextBoardItems : []   // 다음 게시글 정보
      },
      requestBody: this.$route.query,
      boardNo: this.$route.query.boardNo,
      maxBoardNo:0,                       
      minBoardNo:0,
      minYn : 'N',            //게시글번호 min여부
      maxYn : 'N'             //게시글번호 max 여부
    }
  },
  mounted() {
    if(this.boardNo != 'undefined'){          
      this.fnGetView()                              // 현재 게시글 정보
      this.fnGetMaxNo('max', this.boardNo)          // 다음글이 max인지 조회
      this.fnGetMaxNo('min', this.boardNo)          // 이전글이 min인지 조회
      this.fnGetBoardDirection(this.boardNo,'pre')  //  이전게시글 정보
      this.fnGetBoardDirection(this.boardNo,'next') //  다음게시글 정보
    }
  },

  methods: {

    // 게시글 정보 + 유저 소개글 + 게시글 이미지
    fnGetView(boardNo) {
      if(boardNo){
        this.boardNo = boardNo;
        this.requestBody.boardNo = boardNo;
        //console.log('fnGetView() : ' + this.boardNo);
      }
      
      this.$axios.get(this.$boardUrl + "/all/getBoard/" + this.requestBody.boardNo)
      .then((res) => {
        this.state.boardItems.boardTitle = res.data.board.boardTitle
        this.state.boardItems.boardUserId = res.data.board.user.userId
        this.state.boardItems.boardContent = res.data.board.boardContent
        this.state.boardItems.imgPath = res.data.board.imgPath
        this.state.boardItems.updDate = res.data.board.updDate
        this.state.boardItems.regDate = res.data.board.regDate
        this.state.boardItems.tags = res.data.tags
        this.state.boardItems.imgs = res.data.imgs.imgPath
        this.state.boardItems.intro = res.data.board.user.introduction
        this.state.boardItems.name = res.data.board.user.name
        this.state.boardItems.userProfile = res.data.board.user.imgPath
        
      })
      .catch((err) => {
        if (err.message.indexOf('Network Error') > -1) {
          alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
        }
      })
      
    },
    fnList() {      // 목록
      delete this.requestBody.boardNo
      this.$router.push({
        path: '/',
        query: this.requestBody
      })
    },
    fnUpdate() {      // 수정
      // this.requestBody.tag = this.tag
      //this.$route.query.boardNo = this.boardNo
      console.log('requestBody : ' + JSON.stringify(this.requestBody, null, 2));
      console.log('requestBody : ' + this.boardNo);

      this.$router.push({
        name: 'BoardWrite',
        query: this.requestBody
      })
    },
    fnDelete() {    // 삭제
      if (!confirm("삭제하시겠습니까?")) return
      //console.log('url : ' + this.$boardUrl + "/" + this.boardNo);
      const apiUrl = this.$boardUrl + "/admin/delete/";
      this.$axios.delete(apiUrl + this.boardNo)
          .then(() => {
            alert('삭제되었습니다.')
            this.fnList();
          }).catch((err) => {
        console.log(err);
      })
    },
    fnView(boardNo) {   // 이전 혹은 다음게시글 이동
      this.boardNo = boardNo
      this.requestBody.boardNo = boardNo

      this.$router.push({
        name: 'BoardDetail',
        query: this.requestBody
      });
      this.$refs.commentListRef.fnGetCount(boardNo);
      this.$refs.commentListRef.fnGetView(boardNo);
      
      this.fnGetView(boardNo)
      this.fnGetMaxNo('max', boardNo)    // 다음글이 max인지 조회
      this.fnGetMaxNo('min', boardNo)    // 이전글이 min인지 조회
      this.fnGetBoardDirection(boardNo,'pre')  //  이전게시글 정보
      this.fnGetBoardDirection(boardNo,'next') //  다음게시글 정보
    },
    // y-m-d format date
    formatRegDate(date) {
      if (!date) return ''; // date가 undefined이면 빈 문자열을 반환하고 함수를 종료합니다.
      const formattedDate = date.split('T')[0];
      return formattedDate;
    },

    fnGetBoardDirection(boardNo, type){   // 이전게시글, 다음게시글 정보 가져오기
      //console.log('fnGetBoardDirection() : ' + boardNo);
      if(type == 'pre'){
        //console.log('pre 버튼');
        this.$axios.get(this.$boardUrl + '/all/getPreBoard/' + boardNo)
        .then((res) =>{
          this.state.preBoardItems.boardNo = res.data.preBoardNo;
          this.state.preBoardItems.boardTitle = res.data.boardTitle
        })
        .catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }else{
        this.$axios.get(this.$boardUrl + '/all/getNextBoard/' + boardNo)
        .then((res) =>{
          this.state.nextBoardItems.boardNo = res.data.nextBoardNo;
          this.state.nextBoardItems.boardTitle = res.data.boardTitle
        })
        .catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }
    },

    fnGetMaxNo(type, boardNo){      // 게시글번호 max값 조회
      this.boardNo = boardNo;
      if(type === 'max'){
        this.$axios.get(this.$boardUrl + '/all/maxBoardNo')
        .then((res) =>{
          this.maxBoardNo = res.data;
          if(this.boardNo == this.maxBoardNo){
            this.maxYn = 'Y';
            console.log('this.maxYn : ' + this.maxYn);
          }else{
            this.maxYn = 'N';
            console.log('this.boardNo : this.maxBoardNo' + this.boardNo + ':' + this.maxBoardNo);
            console.log('this.boardNo : '+ this.boardNo);
          }
        })
        .catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }else{      // 게시글번호 min값 조회
        this.$axios.get(this.$boardUrl + '/all/minBoardNo')
        .then((res) =>{
          this.minBoardNo = res.data;
       
          if(this.boardNo == this.minBoardNo){
            this.minYn = 'Y';
            console.log('this.minYn : ' + this.minYn);
          }else{
            this.minYn = 'N';
            console.log('this.boardNo : this.minBoardNo' + this.boardNo + ':' + this.minBoardNo);
            console.log('this.boardNo : '+ this.boardNo);
          }
        })
        .catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }
      
    }
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
.board-contents h1{
  margin-top: 50px;
  margin-bottom: 40px;
  font-weight: bold;
  font-size: 2.5em;
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
  font-weight: bold;
  font-size: 1.1em;
}
.seperator1{
  margin-right: 10px;
  margin-left: 10px;
}
.regDate{
  color: #495057;
  font-size: 1.0em;
}

.tag-wrap{
  display: flex; 
  flex-wrap: wrap; 
}

.tagName{
  display: inline-block; 
  background-color: #f4faff;
  border-radius: 10px;
  color: rgb(79, 223, 175);
  font-size: 1.2em;
  padding: 5px; 
  margin-right: 5px; 
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
  color: #495057;
  margin-right: 10px;
  border : none;
  background-color: #fff;
}

.board-contents{
  font-size: 1.5em;
  margin-top: 5em;
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

.profile-userId{
  font-size: 2em;
  margin-bottom: 0.5em;
}

a:link { color: red; text-decoration: none;}
a:visited { color: black; text-decoration: none;}
a:hover { color: blue; text-decoration: underline;}

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
  font-weight: bold;
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