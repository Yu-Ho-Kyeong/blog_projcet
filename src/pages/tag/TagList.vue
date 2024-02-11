<template>
    <ul>
        <li class="tag-name"><a>{{titem.tagName}} (3)</a></li>
    </ul>
</template>

<script>
export default {
	name :'Tag',
    props:{
      titem: Object // 객체 데이터타입 지정
    } 
  }
</script>