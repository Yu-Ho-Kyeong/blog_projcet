<template>
  <div class="board-container">
    <form @submit.prevent="submitForm">
        <textarea v-model="boardTitle" placeholder="제목을 입력하세요" class="board-title"></textarea>
        <div class="seperator"></div>
        <TagInput :tags="tagsFromChild" ref="tagInputRef" />

      <div class="editor-container">
        <div class="editor-page">
          <editor v-model="content"></editor>
        </div>
      </div>

      <div class="board-btn">
        <button type="button" class="btn btn-board-cancle" @click="cancelForm">취소</button>
        <button type="submit" class="btn btn-board-save">저장하기</button>
      </div>
    </form>
  </div>
</template>

<script>
import Editor from '@/pages/board/Editor.vue';
import TagInput from "@/pages/tag/TagInput.vue";
import axios from 'axios';
import store from '@/script/store';
import router from '@/script/router';

import { ref, onMounted } from 'vue';

export default {
  components: { Editor, TagInput },
  setup() {

    const boardUserId = ref('');
    const boardTitle = ref('');
    const content = ref('');
    const tagInputRef  = ref(null);
    const boardNo = ref(0);

    onMounted(() => {
        if (store.state.account) {
          boardUserId.value = store.state.account.userId;
          console.log('세션 데이터 : ' + JSON.stringify(store.state.account, null, 2));
        } else {
          console.log("no user.value");
        }
    });

    const submitForm = async () => {
      try {
        //console.log("boardTitle : " + boardTitle.value);
        //console.log("content : " + content.value);
        //console.log("id : " + store.state.account.id);
        const tagsFromChild = tagInputRef.value.tags;
        //console.log('Tags from child component:', tagsFromChild);
        
        const requestData = { 
          boardUserId: boardUserId.value,
          boardTitle: boardTitle.value,
          boardContent: content.value,
          tag: tagsFromChild.join(', ')
        };
        console.log('Data to be sent:', requestData);

        const response = await axios.post('/api/user/SaveBoard', requestData, {
          Headers:{
            'Content-Type' : 'application/json', // JSON 형식으로 데이터를 전송하기 위한 헤더 설정
          },
        });
        boardNo.value = response.data;
        console.log(response);
        console.log('boardNo : ' + boardNo.value);

        // 서버에서 올바른 응답을 받으면 게시글 상세 페이지로 이동
        newBoardView(boardNo.value);
        //router.push({path: '/board/detail', params: {boardNo: boardNo.value}});
        //router.push({ path: '/board/detail', params: boardNo.value});
      } catch (error) {
        console.log('글작성 실패', error.response.data);
      }
    }
    // 신규 게시글 작성시 게시글 상세 페이지로 이동
    const newBoardView = (newBoardNo)=> {
      router.push({name: 'BoardDetail', params: {newBoardNo}});
    }

    const cancelForm = () => {
      // 취소 버튼 클릭 시 필요한 로직 추가
    }

    return { boardNo, boardUserId, boardTitle, store, content, tagInputRef, submitForm, cancelForm };
  },
}
</script>

<style>
.board-container {
    max-width: 800px;
    max-height: 800px;
    height: 800px;
    margin: 0 auto;
    padding: 20px;
}

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