import { test } from "node:test";
import assert from "node:assert/strict";

import {
  addressOf,
  builtFileExists,
  descriptionOf,
  loadBuiltPage,
  postAddress,
  postPagePath,
  titleOf,
} from "./support/built-pages.js";
import { POST_LISTS, POSTS, TITLE_ENDING } from "./support/constants.js";

test("each post folder becomes a page", () => {
  for (const post of POSTS) {
    assert.ok(builtFileExists(postPagePath(post)), `No page for the post "${post.folder}"`);
  }
});

test("each post page has the post's title and description", () => {
  for (const post of POSTS) {
    const page = loadBuiltPage(postPagePath(post));
    assert.equal(titleOf(page), `${post.title}${TITLE_ENDING}`);
    assert.equal(descriptionOf(page), post.description);
  }
});

test("each post page shows its title as the one main heading", () => {
  for (const post of POSTS) {
    const headings = loadBuiltPage(postPagePath(post))("h1");
    assert.equal(headings.length, 1, `${post.folder}: expected one main heading`);
    assert.equal(headings.text().trim(), post.title);
  }
});

test("each post page shows the day it was written", () => {
  for (const post of POSTS) {
    const day = loadBuiltPage(postPagePath(post))(`time[datetime="${post.day}"]`);
    assert.equal(day.text().trim(), post.readableDay, `${post.folder}: the day is not shown`);
  }
});

test("each list page has its own title and description", () => {
  for (const list of POST_LISTS) {
    const page = loadBuiltPage(list.path);
    assert.equal(titleOf(page), list.title);
    assert.equal(descriptionOf(page), list.description);
  }
});

test("a card shows a picture only when its post has one", () => {
  for (const list of POST_LISTS) {
    const page = loadBuiltPage(list.path);
    page("main article").each((index, card) => {
      const post = list.posts[index];
      const expected = post.picture ? 1 : 0;
      assert.equal(page(card).find("img").length, expected, `${post.folder}: wrong number of pictures on the card`);
    });
  }
});

test("each list page shows every post, newest first", () => {
  for (const list of POST_LISTS) {
    const page = loadBuiltPage(list.path);
    const listed = page("main article h2")
      .map((_, heading) => page(heading).text().trim())
      .get();
    assert.deepEqual(listed, list.posts.map((post) => post.title), `${list.address}: wrong posts or order`);
  }
});

test("each card links to its post", () => {
  for (const list of POST_LISTS) {
    const page = loadBuiltPage(list.path);
    page("main article").each((index, card) => {
      const link = page(card).find("a").attr("href");
      assert.equal(addressOf(link, list.address), postAddress(list.posts[index]));
    });
  }
});
