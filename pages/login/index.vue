<template>
  <div class="px-10 mt-16">
    <h1 class="title mb-8">Log In</h1>
    <p>
      We don't sell your data because we don't know who to sell it to. Also,
      please don't hack the Elimination website or any related service.
    </p>
    <div class="flex-row flex mt-4" >
    <div class="form-control my-auto">
      <label class="label gap-2 cursor-pointer">
        <input v-model="agreed"
          type="checkbox"
          class="checkbox checkbox-sm checkbox-primary"
        />
        <span class="label-text">I understand</span>
      </label>
    </div>
    <div v-if="agreed" class="my-auto ml-auto">

       <client-only> <a :href="`https://xz.ax/login?redirect_url=${protocol}//${host}/app`" class="btn btn-primary btn-sm">log in</a></client-only>
    </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const agreed = ref(false)
const protocol = ref('')
const host = ref('')
if(!process.server){
  protocol.value = document.location.protocol
  host.value = document.location.host
}

</script>

<style>
</style>