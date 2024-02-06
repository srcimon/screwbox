  let observer = new IntersectionObserver((entries) => {

entries.forEach((entry) => {
    if(entry.isIntersecting) {
       entry.target.classList.add('show');
    } else {
       entry.target.classList.remove('show');
    }
    });
});

let hiddenElements = document.querySelectorAll(".hidden");
hiddenElements.forEach((el) => observer.observe(el));