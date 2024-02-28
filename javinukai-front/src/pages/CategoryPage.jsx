import React, { useEffect, useState } from 'react';
import axios from 'axios';
import CategoryPreview from '../Components/Contest-Components/CategoryPreview';

function CategoryPage() {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    async function fetchCategories() {
      try {
        const response = await axios.get('/api/v1/categories'); // Adjust endpoint as needed
        console.log('Categories from API:', response.data); // Log categories to inspect their structure
        setCategories(response.data);
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    }

    fetchCategories();
  }, []);

  return (
    <div className="category-page">
      <h1>Category Page</h1>
      {/* Check if categories is an array before calling map */}
      {Array.isArray(categories) && categories.map(category => (
        <CategoryPreview key={category.id} category={category} />
      ))}
    </div>
  );
}

export default CategoryPage;

