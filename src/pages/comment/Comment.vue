<template>
  <div class="comment-container">
    <div class="comment-wrapper">
      <h5>{{commentCount}} 개의 댓글</h5>

      <!-- 댓글 작성 -->
      <div class="comment-write">
        <CommentWrite :parentBoardNo ="this.boardNo" :parentUserNo="this.userNo"
                       @comment-submitted="handleCommentSubmitted"/>
      </div>

      <!-- 댓글 리스트 -->
      <div class="registered-comment-wrapper" v-for="(cItem, index) in commentItems" :key="index">
        <div class="registered-comment">
          <div class="comment-profile">
            <img :src="cItem.user.imgPath" alt="comment-user-profile"/>
            <div class="comment-info">
              <div class="userId">
                {{cItem.user.name}}
              </div>

              <div class="regDate">{{formatRegDate(cItem.regDate)}}</div>
            </div>
          </div>

          <div class="common-buttons">
            <button type="button" v-on:click="changeDiv(2, index, cItem)">답글</button>
            <button type="button" v-if="cItem.user.userId === this.$store.state.account.userId" 
                    v-on:click="changeDiv(1, index)">수정</button>
            <button type="button" v-if="cItem.user.userId === this.$store.state.account.userId" 
                    v-on:click="fnDelete(cItem)">삭제</button>
          </div>


          <div class="btn-output-section">
            <!-- default 댓글 내용 -->
            <div class="comment-content" v-if="commentItems[index].divType == 'comment-content'">
              <p>{{cItem.commentContent}}</p>
            </div>

            <!-- 수정 버튼 클릭시 노출되는 div -->
            <div class="comment-modify" v-else-if="commentItems[index].divType == 'comment-modify'">
              <button class="btn_comment_cancle" @click="changeDiv(3, index)">취소</button>
              <button class="btn_comment_modify" @click="fnUpdate(cItem)">수정하기</button>
              <textarea v-model="cItem.commentContent" class="comment-modify-input"></textarea>
            </div>

            <!-- 답글 버튼 클릭시 노출되는 div -->
            <div class="recomment-register" v-else>
              <p>{{cItem.commentContent}}</p>
              <CommentWrite :parentBoardNo ="this.boardNo" :parentUserNo="this.userNo"
                            :parentCommentNo ="this.commentNo" @comment-submitted="handleCommentSubmitted"/>
            </div> 
          </div>

          <!-- 대댓글리스트 : 계층형 댓글 구조를 위한 재귀적인 반복 -->  
          <div class="recomment"> 
            <ReComment :cItem="cItem" :parentBoardNo ="this.boardNo" :parentUserNo="this.userNo"
                       @comment-submitted="handleCommentSubmitted"/>
          </div>
        </div>
      </div>
      <!-- end_for() -->
    </div>
  </div>
</template>

<script>
import ReComment from '@/pages/comment/ReComment.vue';
import CommentWrite from '@/pages/comment/CommentWrite.vue';
export default {
  components: { CommentWrite, ReComment},

  data() {
    return {
      requestBody: this.$route.query,                   
      boardNo: this.$route.query.boardNo,               
      commentNo : 0,
      commentUserId: localStorage.getItem('user_id'),   // 최상위 댓글 저장시 사용
      userNo : localStorage.getItem('user_no'),         // 최상위 댓글 저장시 사용
      commentContent : '',
      commentCount : 0,                                 // 댓글 카운트 
      commentItems: []
    };
  },

  mounted (){
    this.fnGetCount()   //댓글 count
    this.fnGetView()    //댓글 조회
  },

  methods: {
    fnGetCount(boardNo){  // 댓글 COUNT
      if(boardNo){
        this.boardNo = boardNo;
      }
      this.$axios.get(this.$commentUrl + '/all/getCount/' + this.boardNo)
        .then((res) => {
          this.commentCount = res.data
        }).catch((err) => {
          console.log(err)
        })
    },
    fnGetView(boardNo) {  // 댓글 data 불러오기
      if(boardNo){
        this.boardNo = boardNo
        this.commentItems = [];
      }
      
      this.$axios.get(this.$commentUrl + '/all/getComment/' + this.boardNo)
        .then((res) => {
          res.data.forEach(comment => {
            this.commentItems.push(comment);

            //각 댓글의 divType 초기화
            this.commentItems.forEach(commentItem => {
              commentItem.divType = 'comment-content';
            })
          })
          this.fnGetCount();    // 댓글 개수 count

        }).catch((err) => {
          console.log(err)
        })
    },
     
    handleCommentSubmitted(newCommentData){
      // 자식 컴포넌트인 CommentWrite에서 등록한 댓글 data를 통해 새로운 comment 정보 조회
      this.fnGetView(newCommentData.boardNo)
    },

    changeDiv(type, index, cItem) {
      let newDivType = '';
      if(type === 1 ){
        newDivType = 'comment-modify';
      }else if(type === 2 ){
        if(this.commentItems[index].divType === 'comment-write'){
          newDivType = 'comment-content';
        }else{
          newDivType = 'comment-write';
        }
        if(cItem){
          this.commentNo = cItem.id;
        }      
      }else{
        newDivType = 'comment-content';
      }
      //const newDivType = type === 1 ? 'comment-modify' : 'comment-content';
      this.commentItems[index].divType = newDivType;
    },

    fnUpdate(cItem){    // 댓글 수정
      this.commentNo = cItem.id;
      this.commentContent = cItem.commentContent;
      const apiUrl = this.$commentUrl + '/user/modifyComment/' + this.commentNo;
      
      this.$axios.put(apiUrl, {"commentContent"   : this.commentContent}, {
          Headers : {
            'Content-Type' : 'application/json',
          }
        })
        .then(() => {
          alert('댓글이 수정되었습니다.')
          this.fnGetView(this.boardNo);
        }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
    },

    fnDelete(cItem){    // 댓글 삭제
      if(confirm('삭제하시겠습니까?')){
        const commentNo = cItem.id;

        this.$axios.delete(this.$commentUrl + "/user/deleteComment/" + commentNo)
          .then(() => {
            alert('삭제되었습니다.')
            this.fnGetView(this.boardNo);
          }).catch((err) => {
            console.log(err);
          })
      }else{
        return false;
      }  
    },

    formatRegDate(date) {// 등록일 format
      if (!date) return ''; 
      const formattedDate = date.split(' ')[0];
      return formattedDate;
    },
  },
};
</script>

<style scoped>
.comment-container {
  margin-top: 20px;
}

.comment-wrapper {
  padding: 10px;
}

.comment-write-input, 
.re-comment-input {
  resize: none;
  width: 100%;
  box-sizing: border-box;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.buttons-wrapper {
  display: flex;
  justify-content: flex-end;
}

.recomment{
  margin-left:10px;
  width:100%;
}

.recomment-register{
  width: 100%;

}
.btn-comment-write,
.btn-recomment-register,
.btn-recomment-cancle {
  padding: 8px 12px;
  border: 1px solid #007BFF;
  border-radius: 4px;
  background-color: #007BFF;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-comment-write:hover,
.btn-recomment-register:hover,
.btn-recomment-cancle:hover {
  background-color: #0056b3;
}

.common-buttons {
  /* margin-top: 10px; */
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.common-buttons button {
  color: #495057;
  margin-right: 10px;
  border : none;
  background-color: #fff;
}

.registered-comment-wrapper {
  margin-top: 15px;
}

.comment-modify{
  width: 100%;
}

.comment-modify-input {
  resize: none;
  width: 100%;
  box-sizing: border-box;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.comment-profile {
  display: flex;
  align-items: center;
}

.comment-profile img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 1.5em;
}

.comment-info {
  flex: 1;
}

.comment-info .userId {
  font-weight: bold;
}

.regDate {
  color: #777;
}

.comment-content {
  /* margin-top: 2.5rem; */
  margin-bottom: 2.5rem;
  /* border-bottom: 1px; */
}

.recomment {
  padding-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-button a {
  margin-right: 10px;
  color: #007BFF;
  cursor: pointer;
  text-decoration: underline;
}

.comment-button a:hover {
  color: #0056b3;
}

.re-comment-input {
  width: calc(100% - 16px); /* 부모 padding 고려하여 조정 */
}

.btn_comment_cancle { 
  /* padding: 10px 20px; */
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin: 0.5em;
}

.btn_comment_modify { 
  /* padding: 10px 20px; */
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>