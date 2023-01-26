<template>
    <input type="checkbox" id="code-modal" class="modal-toggle" />

    <label for="code-modal" class="modal modal-middle bg-black/40">
        <label class="modal-box flex flex-col overflow-visible rounded-t-lg sm:rounded-lg" for="">
            <QrcodeVue
                :value="`${config.public.apiUrl}/game/eliminate?code=${code}`"
                :size="256"
                :margin="7"
                class="absolute rounded-lg -top-8 left-0 right-0 mx-auto"
            />
            <p class="mt-52 mb-4 text-secondary">
                To get eliminated, have your eliminator scan the QR code above. If it isn't working, try typing in
                the elimination code shown below.
            </p>
            <code class="bg-base-200 rounded-lg p-5 font-mono text-secondary box-border">
                {{ code }}
            </code>
        </label>
    </label>
</template>

<script lang="ts">
import QrcodeVue from 'qrcode.vue';

export default {
    name: "KillCodeModal",
    components: {QrcodeVue},
    props: {
        code: String
    }
}
</script>

<script setup lang="ts">
const config = useRuntimeConfig();
</script>
