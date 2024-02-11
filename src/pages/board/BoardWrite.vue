<template>
  <div class="board-container">
    <!-- <form @submit.prevent="submitForm"> -->
      <textarea v-model="boardTitle" placeholder="제목을 입력하세요" class="board-title"></textarea>
      <div class="seperator"></div>

      <div class="tag-container">
        <TagInput :tags="tagsFromChild" ref="tagInputRef" />
      </div>    

      <div class="editor-container">
        <div class="editor-page">
            <editor v-model="content"></editor>
        </div>
      </div>

      <div class="board-btn">
        <button type="button" v-on:click="fnList" class="btn btn-board-cancle">취소</button>
        <button type="button" v-on:click="fnSave" class="btn btn-board-save">저장하기</button>
      </div>
    <!-- </form> -->
  </div>
</template>

<script>
import Editor from '@/pages/board/Editor.vue';
import TagInput from "@/pages/tag/TagInput.vue";


// import axios from 'axios';
// import router from '@/script/router';
// import store from '@/script/store';

export default {
  components: { Editor, TagInput },
  data() {
    return {
      requestBody: this.$route.query,
      boardNo: this.$route.query.boardNo,
      boardUserId: '',
      boardTitle: '',
      content: '',
      tagsFromChild: [],
      regDate : '',
      tag : ''
    };
  },
  mounted() {
    this.fnGetView()
  },
  methods: {
    fnGetView() {
      if (this.boardNo !== undefined) { // 게시글을 수정할 경우 (boardNo가 존재)
        this.$axios.get(this.$boardUrl + '/' + this.boardNo, {
        params: this.requestBody
        }).then((res) => {
          this.boardTitle = res.data.boardTitle
          this.boardUserId = res.data.boardUserId
          this.content = res.data.boardContent
          this.regDate = res.data.regDate
          this.tag = res.data.tag

          console.log('받아온 data : ' + JSON.stringify(res, null, 2));
          console.log('받아온 content : ' + this.content);

          // TagInput에서 받아온 태그 데이터를 tagsFromChild에 저장
          if(this.tag){
            const type = typeof(this.tag);
            console.log('tagstype : ' + type);
            this.tagsFromChild = this.tag.split(',').map(tag => tag.trim());
            this.$refs.tagInputRef.addTag(this.tagsFromChild);
            console.log('this.tagsFromChild : ' + this.tagsFromChild);
          }else{
            console.log('tag is null');
          }
          

        }).catch((err) => {
          console.log(err)
        })
      }else{
        if (this.$store.state.account) {
          this.boardUserId = this.$store.state.account.userId;
          console.log('세션 데이터 : ' + JSON.stringify(this.$store.state.account, null, 2));
        } else {
          console.log("no user.value");
        }
      }
    },
    
    fnList() {
      delete this.requestBody.boardNo
      this.$router.push({
        path: './board',
        query: this.requestBody
      })
    },

    fnView(boardNo) {
      this.requestBody.boardNo = boardNo
      this.$router.push({
        path: './detail',
        query: this.requestBody
      })
    },

    fnSave() {
      let apiUrl = this.$boardUrl + '/saveBoard'
      const tagsFromChild = this.$refs.tagInputRef.tags;

      console.log("boardTitle : " + this.boardTitle);
      console.log("content : " + this.content);
      console.log("id : " + this.$store.state.account.id);
      console.log('Tags from child component:', tagsFromChild);

      this.form = {
        "boardNo"       : this.boardNo,
        "boardUserId"   : this.boardUserId,
        "boardTitle"    : this.boardTitle,
        "boardContent"  : this.content,
        "tag"           : tagsFromChild.join(', ')
      };
      console.log('전송 데이터:', this.form);

      if(this.boardNo === undefined){
        //INSERT
        this.$axios.post(apiUrl, this.form)
        .then((res) => {
          console.log('savedBoardNo : ' + res.data);
          alert('글이 저장되었습니다.')
          this.fnView(res.data)
        }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }else{
        console.log('수정할 this.boardNo : ' + this.boardNo);
        // UPDATE
        this.$axios.put(apiUrl, this.form)
        .then((res) => {  
          alert('글이 저장되었습니다.')
          console.log('res.data : ' + res.data);
          this.fnView(res.data.boardNo)
        }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }
    },

    // 이벤트 핸들러 추가
    // handleTagAdded(tag) {
    //   this.tagsFromChild.push(tag);
    // },
    // handleTagRemoved(index) {
    //   this.tagsFromChild.splice(index, 1);
    // }
  }
}
</script>

<style>
.board-container {
    max-width: 100%;
    max-height: 800px;
    height: 800px;
    margin: 0 auto;
    padding: 20px;
}
/* .tag-container{
  display: inline-flex;
} */


.board-title {
    background: transparent;
    display: block;
    padding: 10px;
    font-size: 2rem;
    width: 100%;
    resize: none;
    line-height: 1.5;
    outline: none;
    border: none;
    font-weight: bold;
}

.seperator {
    background: rgb(73, 80, 87);
    height: 4px;
    width: 4rem;
    border-radius: 1px;
}

.editor-container {
    width: 100%;
    height: 50%;
    display: flex;
    justify-content: center;
    margin-top: 30px;
    border: none;
    
}

.editor-page {
    width: 100%;
    max-width: 100%; /* 예시로 지정한 최대 폭. 필요에 따라 조절 */
    margin-right: 200px;
    max-height: 1000px;
    height: 100%;
}

.board-btn{
    position: relative;
}

.btn-board-cancle{
    position: absolute;
    top: 100px;
    left: 0px;
}

.btn-board-save{
    position: absolute;
    top: 100px;
    left: 450px;
}


</style>