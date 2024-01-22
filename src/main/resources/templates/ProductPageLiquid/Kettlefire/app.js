var Swiper = new Swiper(".mySwiper", {
  loop: true,
  effect: "fade",
  pagination: {
    el: ".swiper-pagination",
    type: "fraction",
  },
  navigation: {
    nextEl: ".swiper-button-next",
    prevEl: ".swiper-button-prev",
  },
});
