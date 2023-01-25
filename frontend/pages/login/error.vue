<template>
    <div class="flex flex-col text-center p-8 gap-8 pt-24">
        <h1 class="text-accent font-bold text-5xl mx-auto mb-8">Oops!</h1>
        <p class="mx-auto text-xl font-medium ">Sorry, there was a problem logging in. Please try again.</p>
        <div>
            <a
                :href="`${config.public.apiUrl}/login?redirect_url=${config.public.appUrl}/app`"
                class="btn btn-primary btn-sm font-bold w-60 h-10 my-4 min-w-min"
            >
                Log in
            </a>
        </div>
    </div>
</template>

<script setup>
import {ref} from 'vue';

const config = useRuntimeConfig();

const protocol = ref('');
const host = ref('');

if (!process.server) {
    protocol.value = document.location.protocol;
    host.value = document.location.host;
} else {
    console.log('hi')
}
</script>
