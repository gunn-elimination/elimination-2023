<template>
    <h1 class="title">Log In</h1>
    <p>
        By continuing, you agree that you have read and will follow the Elimination rules and will
        not attempt to exploit this website or any related service.
    </p>
    <a :href="`https://xz.ax/login?redirect_url=${protocol}//${host}/app`" class="btn btn-primary btn-sm font-bold w-1/4 h-10 my-4">
        Continue
    </a>
</template>

<script setup>
import { ref } from 'vue';

const agreed = ref(false);
const protocol = ref('');
const host = ref('');

if (!process.server) {
    protocol.value = document.location.protocol;
    host.value = document.location.host;
}
</script>
