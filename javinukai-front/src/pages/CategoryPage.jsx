import { useEffect, useState } from "react";
import axios from "axios";
import CategoryPreview from "../Components/Contest-Components/CategoryPreview";

function CategoryPage() {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    async function fetchCategories() {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_BACKEND}/api/v1/categories`, { withCredentials: true }
        ); 
        console.log("Categories from API:", response.data);
        setCategories(response.data);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    }

    fetchCategories();
  }, []);

  return (
    <div className="category-page">
      <h1>Category Page</h1>
      
      {Array.isArray(categories) &&
        categories.map((category) => (
          <CategoryPreview key={category.id} category={category} />
        ))}
    </div>
  );
}

export default CategoryPage;
