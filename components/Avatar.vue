<template>
    <div v-if="src" class="avatar w-10 my-auto">
        <div
            class="w-full rounded-full ring-primary ring-offset-base-100 ring-offset-2"
            :class={ring:ring}
        >
            <img :src="src" />
        </div>
    </div>
    <div v-else class="avatar placeholder">
        <div class="bg-base-200 text-neutral-content rounded-full w-10">
            <span class="text-xl text-secondary">{{ placeholder }}</span>
        </div>
    </div>
</template>

<script>
export default {
    props: ['src', 'ring', 'placeholder']
}
</script>
