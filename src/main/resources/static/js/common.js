function toggleDarkMode() {
    document.body.classList.toggle("dark-mode");
    // save preference
    localStorage.setItem(
        "theme",
        document.body.classList.contains("dark-mode") ? "dark" : "light"
    );
}
// load theme on every page
window.onload = function () {
    if (localStorage.getItem("theme") === "dark") {
        document.body.classList.add("dark-mode");
    }
};