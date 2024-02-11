<template>
    <div class="tags-input-container">
        <div class="added-tag">
            <div class="tag" v-for="(tag, index) in tags" :key="'tag'+index" @click="removeTag(index)">{{tag}}</div>
        </div>
        <div class="tag-input">
            <input v-model="tagValue" 
                   placeholder="태그를 입력하세요" 
                   @keyup.enter="addTag()" 
                   :class="{ 'none': inputClasses[index] }"/>
            </div>
        
    </div>
</template>

<script>
import { ref } from 'vue';
export default ({

    setup() {
        const tagValue = ref('');
        const tags = ref([]);
        const inputClasses = ref([]);
        const addTag = ()=> {
            if(tagValue.value != '')
                tags.value.push(tagValue.value);
                //inputClasses.value.push(false);
                //inputClasses.value[index] = !inputClasses.value[index];
                tagValue.value='';
        }
        const removeTag = (index)=> {
            console.log(tags.value.length);
            tags.value.splice(index, 1);
        }

        return { addTag, tagValue, tags, removeTag, inputClasses }
    }
    
})
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
