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
    <a :href="`https://xz.ax/login?redirect_url=${protocol}//${host}/app`" class="btn btn-primary btn-sm font-bold w-1/4 h-10 my-4 min-w-min">
        Continue
    </a>
    <div class="text-secondary text-sm">
        <p class="mb-2">
            If you are being returned to this page multiple times, it is because your browser is blocking third-party cookies. We are sorry, and are working to fix this issue. 
        </p>
        <p>
            A temporary workaround is to allow third-party cookies from your browser:
        </p>
        <ol class="list-disc list-outside pl-4">
            <li>Chrome Desktop: click on the eye icon in the right end of your search bar, select "Site not working?," and "Allow cookies".</li>
            <li>iOS Safari: Go to the Settings app, open "Safari" settings, and turn off "Prevent Cross-Site Tracking". Make sure to turn this back on to protect your privacy!</li>
            <li>There is no known workaround for Chrome Mobile.</li>
        </ol>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const protocol = ref('');
const host = ref('');

if (!process.server) {
    protocol.value = document.location.protocol;
    host.value = document.location.host;
}
</script>
