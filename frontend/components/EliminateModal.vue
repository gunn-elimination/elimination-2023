<template>
    <input type="checkbox" id="elim-modal" class="modal-toggle" />

    <label for="elim-modal" class="modal modal-middle bg-black/40">
        <label class="modal-box flex flex-col overflow-visible rounded-t-lg sm:rounded-lg" for="">
            <form
                action="javascript:void(0);"
                @submit="onSubmit"
            >
                <input
                    class="w-full bg-base-200 px-6 py-4 rounded mb-4"
                    placeholder="elimination-code-here"
                    :value="code"
                    @change="(e) => code = e.target.value"
                >
            </form>
            <p class="text-secondary">
                To eliminate someone, type their elimination code into the input above.
            </p>
        </label>
    </label>
</template>

<script lang="ts">
export default {
    name: "EliminateModal"
}
</script>

<script setup lang="ts">
import {useUserStore} from '@/store/user';

const config = useRuntimeConfig();
const store = useUserStore();

const code = ref('');

function onSubmit() {
    fetch(`${config.public.apiUrl}/game/eliminate?code=${code.value}`)
        .then(res => res.json())
        .then(res => {
            if (res.error) return;
            store.refreshTarget();
            store.refreshMe();
        });
    code.value = '';
}
</script>
