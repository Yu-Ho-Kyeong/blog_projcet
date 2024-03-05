<template>
  <div>
    <li class="tag-name">
      <a class="each-tag" >{{titem[0]}} ({{titem[1]}})</a>
    </li> 
  </div>
</template>

<script>
export default {
	name :'Tag',
    props:{
      titem: Object // 객체 데이터타입 지정
    } 
  }

</script>

<style scoped>
  .each-tag{
    cursor: pointer;
  }

</style>



