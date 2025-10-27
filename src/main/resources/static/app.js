function csrfHeaders() {
    const token = document.querySelector('meta[name="_csrf"]')?.content;
    const header = document.querySelector('meta[name="_csrf_header"]')?.content;
    return token && header ? { [header]: token } : {};
}

// Read & render all address books on the home page
async function loadBooks() {
    const list = document.querySelector("#books-list");
    if (!list) return;
    try {
        const res = await fetch("/addressbooks", { headers: { Accept: "application/json" } });
        if (!res.ok) throw new Error(await res.text());
        const books = await res.json();
        list.innerHTML = books
            .map(b => `<li><a href="/addressbooks/${b.id}">AddressBook ${b.id}</a></li>`)
            .join("");
    } catch (e) {
        console.error(e);
        alert("Failed to load address books.");
    }
}

// Create a new address book from the home page form
function wireCreateBookForm() {
    const form = document.querySelector("#create-ab-form");
    if (!form) return; // Not on home page
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        try {
            const res = await fetch(form.getAttribute("action") || "/addressbooks", {
                method: "POST",
                headers: { "Content-Type": "application/json", Accept: "application/json", ...csrfHeaders() },
                body: "{}"
            });
            if (!res.ok) throw new Error(await res.text());
            await loadBooks(); // refresh the list
        } catch (e) {
            console.error(e);
            alert("Failed to create address book.");
        }
    });
}

// Add a buddy from the book page form
function wireAddBuddyForm() {
    const form = document.querySelector("#add-buddy-form");
    if (!form) return;
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const nameEl = form.querySelector("#name");
        const addrEl = form.querySelector("#address");
        const name = nameEl.value.trim();
        const address = addrEl.value.trim();
        if (!name || !address) return;

        try {
            const res = await fetch(form.getAttribute("action"), {
                method: "POST",
                headers: { "Content-Type": "application/json", Accept: "application/json", ...csrfHeaders() },
                body: JSON.stringify({ name, address })
            });
            if (!res.ok) throw new Error(await res.text());

            // Append to the list without reloading
            const ul = document.querySelector("#buddies-list");
            if (ul) {
                const li = document.createElement("li");
                li.textContent = `${name} - ${address}`;
                ul.appendChild(li);
            }
            nameEl.value = "";
            addrEl.value = "";
        } catch (e) {
            console.error(e);
            alert("Failed to add buddy.");
        }
    });
}

// Hook everything up once the page is ready
document.addEventListener("DOMContentLoaded", () => {
    wireCreateBookForm();
    wireAddBuddyForm();
    loadBooks();
});
