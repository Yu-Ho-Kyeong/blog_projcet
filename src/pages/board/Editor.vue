<template>
  <div class="editor-page">
    <!-- <quill-editor  :value="state.editorContent"
                  ref="myTextEditor"
                  v-model="state.editorContent" 
                  @input="updateContent" 
                  @update:content="onEditorChange"
                  @change="onEditorChange"
                  @image-added="onImageAdded"
                  >
    </quill-editor> -->
    <quill-editor :value="state.editorContent"
                  class="editor"
                  ref="myTextEditor"
                  v-model="state.editorContent" 
                  :options="editorOption"
                  @blur="onEditorBlur($event)"
                  @focus="onEditorFocus($event)"
                  @ready="onEditorReady($event)"
                  @update:content="onEditorChange"
                  @change="onEditorChange"
                  @image-added="onImageAdded"
                  @update:file="onImageAdded"
                  >
    </quill-editor>

    <!-- <div class="output ql-snow right">
      <div v-html="state.editorContent"></div>
    </div> -->
  </div>

  
</template>

<script>

export default {
  name: 'Editor',
  props: {
    content       : String,
    parentFile    : Object,
    parentBoardNo : Object
  },
  data() {
    return {
      state: {
        editorContent   : this.content,
        content         : '',
        file            :  [],
        boardNo         : parseInt(this.parentBoardNo),
        _content        : '',
        editorOption    : {
            placeholder   : '',
            modules       : {
              toolbar       : [
                ['bold', 'italic', 'underline', 'strike'],
                ['blockquote', 'code-block'],
                [{ header: 1 }, { header: 2 }],
                [{ list: 'ordered' }, { list: 'bullet' }],
                [{ script: 'sub' }, { script: 'super' }],
                [{ indent: '-1' }, { indent: '+1' }],
                [{ direction: 'rtl' }],
                [{ size: ['small', false, 'large', 'huge'] }],
                [{ header: [1, 2, 3, 4, 5, 6, false] }],
                [{ color: [] }, { background: [] }],
                [{ font: [] }],
                [{ align: [] }],
                ['clean'],
                ['link', 'image']
              ],
            
            }
        },
        disabled: false
      }
    };
  },
  mounted() {
  },
  methods: {
    onEditorBlur(quill) {
      console.log('editor blur!', quill);
    },
    onEditorFocus(quill) {
      console.log('editor focus!', quill);
    },
    onEditorReady(quill) {
      console.log('editor ready!', quill);
      this.registerImageHandler(quill)
    },
    onEditorChange({ quill, html, text }) {
      console.log('editor change!', quill, html, text);
      this.state._content = html;
      this.state.content = html;
      this.state.editorContent = html;
      this.$emit('update:content', html);
    },

    registerImageHandler(quill) {
      const quillEditor = quill;

      if (quillEditor) {
        const toolbar = quillEditor.getModule('toolbar');
        if (toolbar) {  
          toolbar.addHandler('image', () => { // editor 이미지 추가시 핸들러
            const fileInput = document.createElement('input');
            fileInput.setAttribute('type', 'file');
            fileInput.setAttribute('accept', 'image/*');
            fileInput.click();
            fileInput.addEventListener('change', () => {
              const file = fileInput.files[0];
              if (file) {     
                this.onImageAdded(file, quill); 
              }
            });
          });
        }
      } else {
        console.log('Quill editor not found.');
      }
    },

    onImageAdded(file, quill) {   // 파일 추가시 체크 매서드
      const allowedExtensions = ['jpg', 'jpeg', 'png', 'gif']; 
      // 파일 이름에서 확장자 추출
      const fileName = file.name;
      const fileSize = file.size;
      const maxSize = 20 * 1024 * 1024;
      const fileExtension = fileName.split('.').pop().toLowerCase();

      if (!allowedExtensions.includes(fileExtension)) {
        alert('이미지 파일만 첨부 가능합니다.');
        return;
      }

      if (fileSize > maxSize) {
        alert("업로드 가능한 최대 이미지 용량은 최대 20MB입니다.");
        return;
      }

      const reader = new FileReader();
      reader.onload = (e) => {
        const imageDataUrl = e.target.result;
        console.log('imageDataUrl : ' + imageDataUrl);
        const quillEditor = quill;
        const range = quillEditor.getSelection();
        quillEditor.insertEmbed(range.index, 'image', imageDataUrl);
        //console.log('quillEditor.root.innerHTML : ' + quillEditor.root.innerHTML);
        this.state.editorContent = quillEditor.root.innerHTML;
        this.state.file.push(file);
        this.$emit('update:file', file);    // 이벤트를 통한 부모 컴포넌트에 변수 전달
      }
      //파일을 읽어서 Base64로 변환하여 보여줌
      reader.readAsDataURL(file);
    }
  },
  watch: {
    content(newValue) {
      console.log('부모 컴포넌트 변경 ', newValue);
      this.state.editorContent = newValue;
      this.$emit('update:content', newValue);
    },
    'state.content'(newValue) {
      console.log('자식 컴포넌트 변경 ', newValue);
      this.state.content = newValue;
      this.state.editorContent = this.state.content;
      this.$emit('update:content', newValue);
    }
  }
}
</script>

<style scoped>

</style>