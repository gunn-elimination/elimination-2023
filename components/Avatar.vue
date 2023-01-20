<template>
    <div class="avatar w-10 my-auto">
        <div
            class="w-full rounded-full ring-primary ring-offset-base-100 ring-offset-2"
            :class={ring:ring}
        >
            <img :src="src" />
        </div>
    </div>
</template>

<script>
export default {
    props: ['src', 'ring']
}
</script>
