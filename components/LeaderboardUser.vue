<template>
    <li class="flex gap-4 items-center">
        <Avatar src="https://placeimg.com/192/192/people?e=p" />
        <p class="font-medium">{{ forename }} {{ surname }}</p>
        <p class="text-secondary italic">{{ eliminations }} eliminations</p>
    </li>
</template>

<script>
export default {
    name: "LeaderboardUser",
    props: {
        forename: String,
        surname: String,
        eliminations: Number
    }
}
</script>
