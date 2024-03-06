<template>
  <div class="board-container">
    <!-- <form @submit.prevent="submitForm"> -->
      <textarea v-model="boardTitle" placeholder="제목을 입력하세요" class="board-title"></textarea>
      <div class="seperator"></div>

      <div class="tag-container">
        <TagInput :tags="tagsFromChild" ref="tagInputRef" @update:tags="updateTag"  />
      </div>    

      <div class="editor-container">
        <div class="editor-page">
            <!-- //<editor v-model="content"></editor> -->
            <!-- <Editor :content="contentFromChild" @update:content="onEditorChange" /> -->
            <Editor :content="contentFromChild" 
                    :parentFile="filesFromChild"
                    :parentBoardNo="this.boardNo" 
                    @update:content="onEditorChange"
                    @update:file="onImageAdded"/>
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
import base64 from '@/script/util/base64.js';
export default {
  components: { Editor, TagInput },
  data() {
    return {
      requestBody         : this.$route.query,
      boardNo             : this.$route.query.boardNo,
      boardUserId         : '',
      userNo              : localStorage.getItem('user_no'),
      boardTitle          : '',
      content             : '',
      contentFromChild    : '',
      filesFromChild      : [],
      tagsFromChild       : [],
      regDate             : '',
      tags                : '',
      uploadData          : []
    };
  },
  mounted() {
    if (this.boardNo !== undefined) { 
      this.fnGetView()      // 게시글 수정시 해당 게시글 정보 불러오기
    }
  },
  methods: {
    fnGetView() {
      if (this.boardNo !== undefined) { // 게시글을 수정할 경우 (boardNo가 존재)
        this.$axios.get(this.$boardUrl + '/all/getBoard/' + this.boardNo).then((res) => {
          this.boardTitle = res.data.board.boardTitle
          this.boardUserId = res.data.board.boardUserId
          this.contentFromChild = res.data.board.boardContent
          this.regDate = res.data.board.regDate
          this.tags = res.data.tags

          // TagInput에서 받아온 태그 데이터를 tagsFromChild에 저장
          if(this.tags != null){
            for(const key in this.tags){
              if(Object.hasOwnProperty.call(this.tags, key)){
                this.tagsFromChild = this.tags[key].tagName;
                //console.log('tag-for : ' + this.tagsFromChild);
                this.$refs.tagInputRef.addTag(this.tagsFromChild);  // 받아온 tag를 하나씩 add하여 노출
              }
            }
          }
        }).catch((err) => {
          console.log(err)
        })
      }else{
        if (this.$store.state.account) {
          this.boardUserId = localStorage.getItem('user_id');
        }
      }
    },
    
    fnList() {
      this.$router.push({
        path: './detail',
        query: this.requestBody
      })
    },

    fnView(boardNo) {
      this.requestBody.boardNo = boardNo
      this.$router.push({
        name: 'BoardDetail',
        query: this.requestBody
      });
    },

    onEditorChange(content) {
      this.contentFromChild = content;
      //console.log('this.contentFromChild : ' + this.contentFromChild);
    },

    onImageAdded(file){
      console.log('file : ' + file);
      this.filesFromChild.push(file);
      //this.filesFromChild=file;
      this.filesFromChild.forEach(file =>{
        console.log('this.filesFromChild : ' + file);
        console.log('file_type1 : ' + typeof file);
      })
    },

    fnSave() {
      //console.log('fnSave()');
      const apiUrl = this.$boardUrl + '/admin/saveBoard'
      const tagsFromChild = this.$refs.tagInputRef.tags; // 사용자 입력 tag data
      // 신규 게시글 저장 boardContent에 img태그가 포함되어 있어 일단 제외하고 저장
      if(this.boardNo === undefined){ 
        let form = {}
        if(this.filesFromChild.length > 0) {    // 받아온 이미지 파일이 있으면  
          form = {
            "boardNo"       : this.boardNo,
            "userNo"        : this.userNo,
            "boardTitle"    : this.boardTitle,
            //"boardContent"  : this.contentFromChild,
            //"tags"          : tagsFromChild.join(', ') //문자열로 전송
            "tags"          : tagsFromChild // 배열형태로 전송
          };
        }else{
          form = {
            "boardNo"       : this.boardNo,
            "userNo"        : this.userNo,
            "boardTitle"    : this.boardTitle,
            "boardContent"  : this.contentFromChild,
            "tags"          : tagsFromChild // 배열형태로 전송
          };
        }
        
        console.log('전송 데이터:', form);

        this.$axios.post(apiUrl, form, {
          headers : {
            'Content-Type' : 'application/json',
          }
        })
        .then((res) => {    // 백엔드에서 게시글 저장후 생성된 게시글 번호 반환
          this.boardNo = res.data;
          if(this.filesFromChild.length > 0) {    // 받아온 이미지 파일이 있으면
            this.uploadData = new FormData();
            this.filesFromChild.forEach(file => {
              this.uploadData.append('files', file);
            });
            this.uploadData.append('boardNo', this.boardNo);

            this.$axios.post(this.$boardUrl+'/admin/uploadImg', this.uploadData, {
              headers : {
                'Content-Type': 'multipart/form-data'
              }
            }).then((res) => { // 업로드된 파일 경로 반환
              if(this.contentFromChild) {
                // editor에서 받아온 html로 구성된 content에서 img태그만 추출
                const imgSources = base64.imginHtmlChk(this.contentFromChild);    
                if(imgSources) { // 추출한 배열 base64 -> 서버에서 생성한 url로 변환 
                  this.boardContent = base64.updateBase64ToServerUrl(this.contentFromChild, imgSources, res.data);
                  let imgPathList = [];
                  imgPathList=res.data;     // board_img 테이블에 들어갈 이미경로 배열
                  //console.log('this.boardContent : ' + this.boardContent);

                  const form = {
                    'boardContent' : this.boardContent,   // img-src 속성이 base64->url로 변환된 content
                    'imgPathList'  : imgPathList,
                    'boardNo'      : this.boardNo
                  } 
  
                  const apiUrl = this.$boardUrl + '/admin/saveImg';
                  this.$axios.post(apiUrl, form, {
                    headers : {
                      'Content-Type' : 'application/json',
                    }
                  })
                  .then((res)=>{
                    this.fnView(res.data.board.boardNo)   // 저장한 게시글의 정보를 다시 불러옴
                  }).catch((err)=>{
                    if (err.message.indexOf('Network Error') > -1) {
                      alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
                    }else if(err.response && err.response.status === 500){
                      alert('error');
                    }else if(err.response && err.response.status === 403){
                        alert('권한이 없습니다. 관리자에게 문의해주세요');
                    }
                  })
                }
              }
            }).catch((err) => {
              if (err.message.indexOf('Network Error') > -1) {
                alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
              } else if(err.response && err.response.status === 500){
                alert('error');
              }
            });
          }else{
            
            this.fnView(res.data)   // 저장한 게시글의 정보를 다시 불러옴
          }
          alert('글이 저장되었습니다.')
        }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }else{    // 게시글 수정
        const form = {
          "boardNo"       : this.boardNo,
          "boardTitle"    : this.boardTitle,
          "boardContent"  : this.contentFromChild,
          //"tags"          : tagsFromChild.join(', ') //문자열로 전송
          "tags"          : tagsFromChild // 배열형태로 전송
        };
        //console.log('전송 데이터:', form);
        this.$axios.put(apiUrl, form,{
          headers : {
            'Content-Type' : 'application/json',
          }
        })
        .then((res) => {  
          alert('글이 저장되었습니다.')
          //console.log('res.data : ' + JSON.stringify(res.data));
          this.fnView(res.data.boardNo)
        }).catch((err) => {
          if (err.message.indexOf('Network Error') > -1) {
            alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
          }
        })
      }
    },
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