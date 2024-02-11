<template>
  <div class="comment-container">
    <div class="comment-wrapper">
    <h5>{{commentCount}} 개의 댓글</h5>
      <!-- 댓글 등록 -->
      <div class="comment-write">
        <textarea
          placeholder="댓글을 작성하세요"
          class="comment-write-input"
          style="height: 70px;"
          v-model="commentContent"
        ></textarea>
        <div class="buttons-wrapper">
          <button class="btn btn-comment-write" @click="writeComment">댓글 작성</button>
        </div>
      </div>

      <!-- 등록된 댓글 -->
      <div class="registered-comment-wrapper" v-for="(cItem, index) in commentItems" :key="index">
        <div class="registered-comment">
          <div class="comment-profile">
            <img
              src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAASbSURBVHgB7Z0tTytBFIYP914BDiQ4cIADB0EhwYFE8ifq7g/hJ2CRSCQ4kOCobF3ruHk3maS5aSnbdnfPOe/7JE0oCTvTnmc+dvbMsNbr9b5M0PLLBDUSgBwJQI4EIEcCkCMByJEA5EgAciQAORKAHAlAjgQgRwKQIwHIkQDkSAByJAA5EoAcCUCOBCBHApAjAciRAORIAHIkADkSgBwJQI4EIEcCkCMByJEA5EgAciQAOX+MhPX1dTs+Prbt7W3b3d21jY2N6ndgPB7bYDCw4XBor6+v9vHxUb1nIL0Ae3t7dn5+XgV9FhABYuC1v79f/Q4SPD8/28vLi2UmrQA/Cfx34O/wwjXu7u7S9gi/z87O/loyELTr62vb2tqyZcFQcXp6Wv2MXiEb6SaBCDwEWDVFqmykEgABOjo6sqbAtbNJkEaAi4uLRoNfQBmXl5eWhRQCIChlnG6Dk5OTVstrkvACYKLXxJg/D5RZ1hEiE14ABGIVs/26IPgZeoHQAiDwbYz7s4AA0XuB0AIsusizKsrycmRCC+Dhyz84OLDIhBUAra/rHgCgDpGHgbAC7O"
              alt="comment-user-thumbnail"
            />
            <div class="comment-info">
              <div class="userId">
                {{cItem.commentUserId}}
                <!-- <a href="https://velog.io/@mk_/posts" class="user-link">{{cItem.commentUserId}}</a> -->
              </div>
              <div class="regDate">{{formatRegDate(cItem.regDate)}}</div>
            </div>
          </div>
          <!-- 댓글 내용 -->
          <div class="comment-content">
            <p>{{cItem.commentContent}}</p>
          </div>
        </div>
        

        <div class="recomment">
          <div class="comment-button">
            <a @click="toggleReplies">숨기기</a>
            <a @click="showReplyForm">답글 달기</a>
          </div>

          <div v-if="showReplyForm" class="recomment-register">
            <textarea placeholder="댓글을 작성하세요" class="re-comment-input" style="height: 70px;"></textarea>
            <div class="buttons-wrapper">
              <button class="btn btn-recomment-cancle" @click="cancelReply">취소</button>
              <button class="btn btn-recomment-register" @click="writeReply">댓글 작성</button>
            </div>
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
      showReplyForm: false,
      requestBody: this.$route.query,
      boardNo: this.$route.query.boardNo,
      loginId: '',
      commentNo : 0,
      commentUserId : '',
      commentContent : '',
      commentCount : 0,
      commentItems: []

    };
  },

  mounted (){
    if (this.$store.state.account) {
      this.loginId = this.$store.state.account.userId;
      console.log('세션 데이터 : ' + JSON.stringify(this.$store.state.account, null, 2));
    } else {
      console.log("no user.value");
    }
    this.fnGetCount()   //댓글 count
    this.fnGetView()    //댓글 조회
  },

  methods: {
    // 댓글 COUNT
    fnGetCount(){
      this.$axios.get(this.$commentUrl + '/getCount/' + this.boardNo, {
        params: this.requestBody
        }).then((res) => {
          this.commentCount = res.data
          console.log('받아온 data : ' + JSON.stringify(res, null, 2));
        }).catch((err) => {
          console.log(err)
        })
    },
    fnGetView() { // 댓글 data 불러오기
      console.log('fnGetView_comment');
      this.$axios.get(this.$commentUrl + '/getComment/' + this.boardNo, {
        params: this.requestBody
        }).then((res) => {
          this.commentItems = res.data
          this.commentNo = res.data.commentNo
          this.boardNo = res.data.boardNo
          this.commentUserId = res.data.commentUserId
          this.commentContent = res.data.commentContent
          this.regDate = res.data.regDate
          console.log('받아온 data : ' + JSON.stringify(res.data, null, 2));
          console.log('this.commentItems : ' + JSON.stringify(this.commentItems, null, 2));
          // console.log('받아온 commentNo : ' + res.data.commentNo);
          // console.log('받아온 commentUserId : ' + res.data.commentUserId);
          // console.log('받아온 commentContent : ' + res.data.commentContent);

        }).catch((err) => {
          console.log(err)
        })
    },
    writeComment() {  // 댓글 작성
      if (!this.commentContent.trim()) {
        alert('댓글 내용을 입력해주세요.');
        return;
      }

      this.form = {
        "boardNo"         : this.boardNo,
        "commentUserId"   : this.commentUserId,
        "commentContent"  : this.commentContent
      };
      console.log('전송 데이터:', this.form);
      let apiUrl = this.$commentUrl + '/addComment'
      this.$axios.post(apiUrl, this.form)
      .then((res) => {
          console.log('commentNo : ' + res.data.commentNo);
          alert('댓글이 저장되었습니다.')
          //this.fnView(res.data)
        }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }else{
            console.log(err.message)
          }
        })

      
      // 여기에 댓글을 전송하는 로직 추가
      // 예를 들어, Axios를 사용하여 서버에 댓글을 전송하는 등의 작업을 수행할 수 있습니다.
      
      // 댓글 작성 후에 댓글 입력 상자 비우기
      this.commentContent = '';
    },
    toggleReplies() {
      // 답글 숨기기 로직
    },
    showReplyFormFunc() {
      this.showReplyForm = true;
    },
    cancelReply() {
      this.showReplyForm = false;
    },
    writeReply() {
      // 답글 작성 로직
    },
    // y-m-d format
    formatRegDate(regDate) {
      const formattedDate = new Date(regDate).toISOString().split('T')[0];
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

.registered-comment-wrapper {
  margin-top: 15px;
}

.comment-profile {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.comment-profile img {
  width: 10%; /* 이미지 크기를 반으로 줄임 */
  border-radius: 50%;
  margin-right: 20px;
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
  margin-top: 2.5rem;
  margin-bottom: 2.5rem;
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
</style>

