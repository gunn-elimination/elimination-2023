<template>
    <div class="card-body">
        <h2 class="card-title">
            {{ title }} | {{ startDate }}
            <span class="badge badge-primary" v-if="unread">NEW</span>
        </h2>
        <p class="whitespace-pre-wrap">
            {{ body }}
        </p>
        <div class="card-actions justify-end" v-if="unread">
            <button class="btn btn-sm btn-primary">mark as read</button>
        </div>
    </div>
</template>

<script>
export default {
    name: "Announcement",
    props: {
        title: String,
        body: String,
        startDate: String,
        endDate: String,
        unread: Boolean
    }
}
</script>
