<template>
    <div
        class="flex items-center justify-center p-6 2xl:p-8 relative border border-gray-300/10"
        :class="!this.$slots.default && 'bg-base-200'"
    >
        <span class="absolute text-sm text-secondary top-2.5 left-3">
            {{ date }}
        </span>
        <p class="text-sm 2xl:text-base" :class="bold && 'font-bold'">
            <slot />
        </p>
    </div>
</template>

<script>
export default {
    name: "CalendarCell",
    props: {
        date: {
            type: String,
            required: true
        },
        bold: Boolean
    }
}
</script>
