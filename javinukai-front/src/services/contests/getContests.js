export default async function (page, limit, sortBy, sortDesc, name) {

  const res = await fetch(
    `${
      import.meta.env.VITE_BACKEND
    }/api/v1/contests?page=${page}&limit=${limit}&sortBy=${sortBy}&sortDesc=${sortDesc}${
      name ? "&name=" + name : ""
    }`,
    {
      method: "GET",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "User-Agent": "react-front",
      },
    }
  );
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.title);
  }

  const data = await res.json();
  console.log(data);

  return data;
}
