<template>
  <div class="p-8 h-full overflow-auto">
    <h1 class="title mb-8">Announcements</h1>
      <div class="card card-compact w-full">
  <div class="card-body">
    <h2 class="card-title">January 26, 2023  <div class="badge badge-secondary">NEW</div></h2>
    <p class="whitespace-pre-wrap">Long announcement. Long announcement. Long announcement. Long announcement.
       Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement.
       Long announcement. Long announcement. Long announcement. Long announcement. </p>
    <div class="card-actions justify-end">
      <button class="btn btn-sm btn-primary">mark as read</button>
    </div>
  </div>

  <div class="card-body">
    <h2 class="card-title">January 25, 2023  <div class="badge badge-secondary">NEW</div></h2>
    <p class="whitespace-pre-wrap">Long announcement. Long announcement. Long announcement. Long announcement.
       Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement.
       Long announcement. Long announcement. Long announcement. Long announcement. </p>
    <div class="card-actions justify-end">
      <button class="btn btn-sm btn-primary">mark as read</button>
    </div>
  </div>

  <div v-for="n in 5" :key="n" class="card card-compact w-full">
  <div class="card-body">
    <h2 class="card-title">January 2{{5-n}}, 2023</h2>
    <p class="whitespace-pre-wrap">Long announcement. Long announcement. Long announcement. Long announcement.
       Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement.
       Long announcement. Long announcement. Long announcement. Long announcement. </p>
  </div>
</div>
</div>

    </div>
</template>

<script>
export default {};
</script>

<style>
</style>