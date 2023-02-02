<template>
    <h1 class="title">Log In</h1>
    <div>
        <p class="mb-2">
            By continuing, you agree that you have read and will follow the Elimination rules and will
            not attempt to exploit this website or any related service.
        </p>
        <p class="mb-2">
            Please log in using your pausd.us email address.
        </p>
    </div>
    <a
        :href="`${config.public.apiUrl}/login?redirect_url=${config.public.appUrl}/app`"
        class="btn btn-primary btn-sm font-bold w-60 h-10 my-4 min-w-min"
    >
        Continue
    </a>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const config = useRuntimeConfig();

const protocol = ref('');
const host = ref('');

if (!process.server) {
    protocol.value = document.location.protocol;
    host.value = document.location.host;
}
</script>
