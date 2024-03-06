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

  .tag-name{
    margin-bottom: 5px;
    list-style:none;
  }
  .each-tag{
    cursor: pointer;
    color: black;
    text-decoration: none;
  }
  .each-tag:hover{
    text-decoration: underline;
  }

</style>



