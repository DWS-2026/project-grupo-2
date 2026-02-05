//CODE IF ADMIN
const params = new URLSearchParams(window.location.search);
const isAdmin = params.get("admin") === "true";

if (!isAdmin){
    document.querySelectorAll(".admin-only").forEach(btn => btn.style.display = "none");
}