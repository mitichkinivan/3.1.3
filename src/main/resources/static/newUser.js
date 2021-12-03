function createUser() {
    let name = document.getElementById("newName").value
    let lastname = document.getElementById("newLastname").value
    let email = document.getElementById("newEmail").value
    let username = document.getElementById("newUsername").value
    let password = document.getElementById("newPassword").value
    let roles = Array.from(document.getElementById("newRoles").selectedOptions).map(options => options.value)

    let userDto = {
        name: name,
        lastname: lastname,
        email: email,
        username: username,
        password: password,
        roles: roles
    }

    console.log(userDto)

    fetch("admin/new", {
        method:"POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userDto)
    }).then(resp => {
        console.log(resp.text());
    }).then(() => showUsersTable())
}