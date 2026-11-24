---
layout: base.njk
permalink: search/index.html
eleventyExcludeFromCollections: true
title: Search
description: "Search every market check, note and concept on the site."
---

# Search

<link rel="stylesheet" href="/pagefind/pagefind-ui.css">
<div id="search" class="not-prose"></div>
<noscript>Search needs JavaScript. All entries are in the menu sections.</noscript>
<script src="/pagefind/pagefind-ui.js"></script>
<script>
  new PagefindUI({ element: "#search", showSubResults: true, translations: { placeholder: "Search entries" } });
</script>
