<template>
  <quill-editor v-model="state.content" @change="onEditorChange"></quill-editor>
</template>

<script>


import { reactive } from 'vue'
export default {
  name: 'App',
  props: {
    // v-model을 사용할 때는 modelValue와 update:modelValue를 정의
    modelValue: String,
  },
  setup(props, context) {
    const state = reactive({
      content: props.modelValue || '',
      _content: '',
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
        // more options
      },
      disabled: false
    })
    
    const onEditorBlur = (quill) => {
      console.log('editor blur!', quill)
    }
    const onEditorFocus = (quill) => {
      console.log('editor focus!', quill)
    }
    const onEditorReady = (quill) => {
      console.log('editor ready!', quill)
    }
    const onEditorChange = ({ quill, html, text }) => {
      console.log('editor change!', quill, html, text)
      state._content = html
      state.content = html;  // state.content를 직접 업데이트
      context.emit('update:modelValue', html);
    }

    setTimeout(() => {
      state.disabled = true
    }, 2000)
    console.log("test : " + state.content);
    console.log("test2 : " + state._content);
    return { state, onEditorBlur, onEditorFocus, onEditorReady, onEditorChange }
  }
}
</script>