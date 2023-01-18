<template>
  <game-has-not-begun/>
  <!-- ENABLE THIS AFTER EVERYTHING IS DONE --->
  <!--<NuxtPage/>--->
</template>
<script >
definePageMeta({
  layout: "game",
  middleware:"elim-auth"
});

</script>
