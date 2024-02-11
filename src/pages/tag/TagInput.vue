<template>
  <div class="tags-input-container">
    <div class="added-tag">
      <div class="tag" v-for="(tag, index) in tags" :key="'tag'+index" @click="removeTag(index)">{{tag}}</div>
    </div>

    <div class="tag-input">
      <input v-model="tagValue" placeholder="태그를 입력하세요" @keyup.enter="addTag(tagValue)" :class="{ 'none': inputClasses[index] }" />
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
      tagValue: '',
      tags: [],
      inputClasses: [],
      v: '',
    };
  },
  // mounted(){
  //   if(this.tagsFromChild.length !== 0){
  //     console.log('this.tagsFromChild : ' + this.tagsFromChild.length);
  //     this.tags = [...this.tagsFromChild];
  //   }else{
  //     console.log('this.tagsFromChild2 : ' + this.tagsFromChild.length);
  //   }
  // },
  methods: {
    addTag(tagValue) {
      if (this.tagValue !== '') {
        this.tags.push(this.tagValue);
        console.log('this.tagValue : ' + this.tagValue);
        this.$emit('tag-added', this.tagValue)
        this.tagValue = '';
        console.log('this.tagValue : ' + this.tagValue);
      }else{
        if(Array.isArray(tagValue)){
          //console.log('tagValue : ' + JSON.stringify(tagValue, null, 2));
          
          console.log('length : ' + tagValue.length);
          for(let v=0; v<tagValue.length; v++){
            //this.tagValue = tagValue.join(', ')
            console.log('tagValue['+v+'] : ' + tagValue[v]);
            this.tagValue = tagValue[v];
            this.tags.push(this.tagValue);
          }
          
        }

        console.log('this.tagValue2 : ' + this.tagValue);
        this.$emit('tag-added', this.tagValue)
        this.tagValue = '';
        
      }
    },
    removeTag(index) {
      this.$emit('tag-removed', index);
      this.tags.splice(index, 1);
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
        /* background-color: rgba(#8b8787, .7); */
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