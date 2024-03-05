<template>
  <div class="tags-input-container">
    <div class="added-tag">
      <div class="tag" v-for="(tag, index) in tags" :key="'tag'+index" 
                      @click="removeTag(index)">{{tag}}</div>
    </div>

    <div class="tag-input">
      <input v-model="tagValue" placeholder="태그를 입력하세요" 
            @keyup.enter="addTag(tagValue)" :class="{ 'none': inputClasses[index] }" 
            @update:tags="updateTag(tagValue)"/>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    tagsFromChild: {
      type: Array,
      default: () => [],
    }
  },
  data() {
    return {
      editTagValue : this.tagsFromChild,
      tagValue: '',
      tags: [],
      inputClasses: [],
      v: '',
    };
  },

  methods: {
    addTag(tagValue) {
      if (this.tagValue !== '') {
        this.tags.push(this.tagValue);
        this.$emit('tag-added', this.tagValue)
        this.tagValue = '';
      }else{
        // 게시글 수정을 통해 db에서 태그를 불러올 경우
        this.tags.push(tagValue);
        this.$emit('tag-added', tagValue)
        this.tagValue = '';
        
      }
    },
    removeTag(index) {
      this.$emit('tag-removed', index);
      this.tags.splice(index, 1);
    },

    updateTag(tagValue){
      this.editTagValue = tagValue;
    }
  },
 
};
</script>

<style scoped>
    .tags-input-container{
        display: flex;
        width: 100%;
        max-width: 600px;
        padding: 10px;
        flex-direction: column;
    }
    .added-tag{
        display: inline-flex;
        flex-wrap: wrap;
        max-inline-size: 100%;
    }
    .tag:last-child{
        margin-right: 0
    }
    input{
        display: inline-flex;
        width: 100%;
        padding: 0;
        margin: 0;
        border: 0;
        outline: none;
        background-color: transparent;
        font-size: 1rem;
    }

    .tag{
        margin-right: 1rem;
        padding: 3px 5px;
        /* float: left; */
        background-color: #f8f9fa46;
        border-radius: 10px;
        cursor: pointer;
    }
    .tag:hover{
        background-color: #57c340;
    }


</style>