import { useState } from 'react';
import styles from './SearchPage.module.css';

const CATEGORIES = ['All', 'Nails', 'Hair', 'Makeup', 'Lashes', 'Skincare', 'Spa', 'Tattoo', 'Brows', 'Waxing'];

const GRADIENTS = [
  'linear-gradient(135deg, #f093fb, #f5576c)',
  'linear-gradient(135deg, #4facfe, #00f2fe)',
  'linear-gradient(135deg, #a18cd1, #fbc2eb)',
  'linear-gradient(135deg, #ffecd2, #fcb69f)',
  'linear-gradient(135deg, #667eea, #764ba2)',
  'linear-gradient(135deg, #ff9a9e, #fecfef)',
  'linear-gradient(135deg, #43e97b, #38f9d7)',
  'linear-gradient(135deg, #fa709a, #fee140)',
  'linear-gradient(135deg, #a1c4fd, #c2e9fb)',
  'linear-gradient(135deg, #d4fc79, #96e6a1)',
  'linear-gradient(135deg, #f6d365, #fda085)',
  'linear-gradient(135deg, #fbc2eb, #a6c1ee)',
  'linear-gradient(135deg, #30cfd0, #330867)',
  'linear-gradient(135deg, #a8edea, #fed6e3)',
  'linear-gradient(135deg, #ff758c, #ff7eb3)',
];

type ExploreItem = {
  id: number;
  gradient: string;
  likes: number;
  comments: number;
  isLarge: boolean;
  isReel: boolean;
  isMulti: boolean;
};

// Generate explore grid with large items pattern
const EXPLORE_ITEMS: ExploreItem[] = Array.from({ length: 30 }, (_, i) => {
  // Pattern: position 2 in first set, position 6 in second set (0-indexed in sets of 9)
  const posInSet = i % 9;
  const setIndex = Math.floor(i / 9);
  const isLarge = (setIndex % 2 === 0 && posInSet === 2) || (setIndex % 2 === 1 && posInSet === 0);

  return {
    id: i + 1,
    gradient: GRADIENTS[i % GRADIENTS.length],
    likes: Math.floor(Math.random() * 15000) + 200,
    comments: Math.floor(Math.random() * 500) + 10,
    isLarge,
    isReel: !isLarge && Math.random() > 0.7,
    isMulti: !isLarge && Math.random() > 0.75,
  };
});

export default function SearchPage() {
  const [activeChip, setActiveChip] = useState('All');
  const [search, setSearch] = useState('');

  return (
    <div className={styles.container}>
      <div className={styles.searchBar}>
        <div className={styles.searchInputWrapper}>
          <span className={styles.searchIcon}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
              <circle cx="11" cy="11" r="7" />
              <line x1="16" y1="16" x2="21" y2="21" />
            </svg>
          </span>
          <input
            className={styles.searchInput}
            placeholder="Search"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
      </div>

      <div className={styles.chips}>
        {CATEGORIES.map((cat) => (
          <button
            key={cat}
            className={activeChip === cat ? styles.chipActive : styles.chip}
            onClick={() => setActiveChip(cat)}
          >
            {cat}
          </button>
        ))}
      </div>

      <div className={styles.grid}>
        {EXPLORE_ITEMS.map((item) => (
          <div key={item.id} className={item.isLarge ? styles.gridItemLarge : styles.gridItem}>
            <div className={styles.gridItemContent} style={{ background: item.gradient }}>
              <svg width={item.isLarge ? '48' : '32'} height={item.isLarge ? '48' : '32'} viewBox="0 0 24 24" fill="currentColor">
                <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z" />
              </svg>
            </div>

            {item.isReel && (
              <span className={styles.reelIndicator}>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="white">
                  <path d="M9.763 17.664a.908.908 0 0 1-.454-.787V11.63a.909.909 0 0 1 1.364-.788l4.631 2.6a.91.91 0 0 1 0 1.575l-4.63 2.6a.91.91 0 0 1-.911.047Z" />
                </svg>
              </span>
            )}

            {item.isMulti && (
              <span className={styles.multiIndicator}>
                <svg width="16" height="16" viewBox="0 0 48 48" fill="white">
                  <path d="M34.8 29.7V11c0-2.9-2.3-5.2-5.2-5.2H11c-2.9 0-5.2 2.3-5.2 5.2v18.7c0 2.9 2.3 5.2 5.2 5.2h18.7c2.8-.1 5.1-2.4 5.1-5.2zM39.2 15v16.1c0 4.5-3.7 8.2-8.2 8.2H14.9c-.6 0-.9.7-.5 1.1 1 1.1 2.4 1.8 4.1 1.8h13.4c5.7 0 10.3-4.6 10.3-10.3V18.5c0-1.6-.7-3.1-1.8-4.1-.5-.4-1.2 0-1.2.6z" />
                </svg>
              </span>
            )}

            <div className={styles.gridOverlay}>
              <span className={styles.gridStat}>
                <svg width="18" height="18" viewBox="0 0 24 24" fill="white">
                  <path d="M16.792 3.904A4.989 4.989 0 0 1 21.5 9.122c0 3.072-2.652 4.959-5.197 7.222-2.512 2.243-3.865 3.469-4.303 3.752-.477-.309-2.143-1.823-4.303-3.752C5.141 14.072 2.5 12.167 2.5 9.122a4.989 4.989 0 0 1 4.708-5.218 4.21 4.21 0 0 1 3.675 1.941c.84 1.175.98 1.763 1.12 1.763s.278-.588 1.11-1.766a4.17 4.17 0 0 1 3.679-1.938Z" />
                </svg>
                {item.likes >= 1000 ? `${(item.likes / 1000).toFixed(1)}k` : item.likes}
              </span>
              <span className={styles.gridStat}>
                <svg width="18" height="18" viewBox="0 0 24 24" fill="white">
                  <path d="M20.656 17.008a9.993 9.993 0 1 0-3.59 3.615L22 22Z" fill="white" />
                </svg>
                {item.comments}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
