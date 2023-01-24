<template>
    <div class="flex flex-col text-center p-8 gap-8 pt-24">
        <h1 class="text-accent font-bold text-5xl mx-auto mb-8">Oops!</h1>
        <p class="mx-auto text-xl font-medium ">Sorry, there was a problem logging in. Please try again.</p>
        <div>
            <client-only>
                <a :href="`/api/login?redirect_url=${protocol}//${host}/app`" class="btn btn-primary btn-lg">
                    log in
                </a>
            </client-only>
        </div>
    </div>
</template>

<script setup>
import {ref} from 'vue';

const protocol = ref('');
const host = ref('');

if (!process.server) {
    protocol.value = document.location.protocol;
    host.value = document.location.host;
} else {
    console.log('hi')
}
</script>
