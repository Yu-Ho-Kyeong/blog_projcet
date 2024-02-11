<template>
  <div class="example">
    <div class="left">
      <quill-editor
        class="editor"
        v-model="editorContent"
        ref="myTextEditor"
        :disabled="false"
        :options="editorOption"
        @change="onEditorChange"
        @blur="onEditorBlur($event)"
        @focus="onEditorFocus($event)"
        @ready="onEditorReady($event)"
      />
    </div>
    
    <div class="output ql-snow right">
      <div v-html="content"></div>
    </div>
  </div>
</template>

<script>


export default {
  name: 'quill-example-snow',
  props:{
    value: String
  },
  data() {
    return {
      editorContent: this.value,
      content: '',
      editorOption: {
        placeholder: '',
        modules: {
          toolbar: [
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
            ['link', 'image', 'video']
          ]
        }
      }
    }
  },
  methods: {
    onEditorBlur(quill) {
      console.log('editor blur!', quill)
    },
    onEditorFocus(quill) {
      console.log('editor focus!', quill)
    },
    onEditorReady(quill) {
      console.log('editor ready!', quill)
    },
    onEditorChange({ quill, html, text }) {
      console.log('editor change!', quill, html, text)
      this.content = html
    }
  },

  watch: {
    value(newValue) {
      this.editorContent = newValue; // 부모 컴포넌트의 content 변경 감지
    },
    editorContent(newEditorContent) {
      this.$emit('input', newEditorContent); // 자식 컴포넌트에서 content 변경 시 이벤트 발생
    }
  }
}
</script>

<style scoped>
.ql-container, .ql-toolbar{
  border: none;

}
.ql-toolbar.ql-snow{
  border: none !important;
}

.example {
  display: flex;
  justify-content: space-between;
  height: 100%;
  
}

.left, .right {
  width: 48%;
  height: 100%;
}

.right {
  padding: 10px;
  overflow-y: auto;
}

.right img{
  max-width:100%;
}
</style>