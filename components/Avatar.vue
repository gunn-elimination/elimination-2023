<template>
    <div class="avatar w-10 my-auto">
        <div
            class="w-full rounded-full ring-primary ring-offset-base-100 ring-offset-2"
            :class={ring:ring}
        >
            <img :src="pfpURL"/>
        </div>
    </div>
</template>

<script>
export default {
    props: ['pfpURL', 'ring']
}
</script>
