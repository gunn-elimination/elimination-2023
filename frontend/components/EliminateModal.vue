<template>
    <input type="checkbox" id="elim-modal" class="modal-toggle" />

    <label for="elim-modal" class="modal modal-middle bg-black/40">
        <label class="modal-box flex flex-col overflow-visible rounded-t-lg sm:rounded-lg" for="">
            <form
                action="javascript:void(0);"
                @submit="onSubmit"
            >
                <input
                    class="w-full bg-base-200 px-6 py-4 rounded"
                    placeholder="elimination-code-here"
                    v-model="code"
                >
                <p v-if="error" class="text-primary text-sm mt-2">
                    There was an error validating your inputted code. Make sure you have the correct code, and it is
                    spelled correctly. <!-- The underlying error is: {{ error }} -->
                </p>
            </form>
            <p class="text-secondary mt-4">
                To eliminate someone, type their elimination code into the input above.
            </p>
        </label>
    </label>
</template>

<script setup lang="ts">
import {useUserStore} from '@/store/user';

const config = useRuntimeConfig();
const store = useUserStore();

const code = ref('');
const error = ref<string | null>(null);

function onSubmit() {
    fetch(`${config.public.apiUrl}/game/eliminate?code=${code.value}`)
        .then(res => {
            if (res.ok) error.value = null;
            else error.value = res.status + res.statusText;
        });
    code.value = '';
}
</script>
