#!/usr/bin/env python3
"""
Graph visualization script for MST Algorithm Analysis
Reads output.csv and generates professional charts
"""

import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import os

# Set style for professional-looking charts
sns.set_style("whitegrid")
plt.rcParams['figure.figsize'] = (12, 6)
plt.rcParams['font.size'] = 11
plt.rcParams['font.family'] = 'sans-serif'

# Create charts directory if it doesn't exist
os.makedirs('charts', exist_ok=True)

print("Loading data from output.csv...")
try:
    df = pd.read_csv('output.csv')
    print(f"✓ Loaded {len(df)} graph results")
except FileNotFoundError:
    print("ERROR: output.csv not found!")
    print("Please run BenchmarkRunner.java first to generate results.")
    exit(1)

# Chart 1: Execution Time Comparison
print("\nGenerating Chart 1: Execution Time vs Graph Size...")
plt.figure(figsize=(14, 7))

plt.plot(df['Vertices'], df['Prim_Time_ms'], 
         marker='s', markersize=8, linewidth=2.5, 
         label="Prim's Algorithm", color='#2E86AB', alpha=0.9)

plt.plot(df['Vertices'], df['Kruskal_Time_ms'], 
         marker='o', markersize=8, linewidth=2.5, 
         label="Kruskal's Algorithm", color='#A23B72', alpha=0.9)

plt.xlabel('Number of Vertices', fontsize=13, fontweight='bold')
plt.ylabel('Execution Time (milliseconds)', fontsize=13, fontweight='bold')
plt.title('MST Algorithm Performance: Execution Time vs Graph Size', 
          fontsize=15, fontweight='bold', pad=20)
plt.legend(fontsize=12, loc='upper left', framealpha=0.95)
plt.grid(True, alpha=0.3, linestyle='--')

# Add annotations for key points
max_prim_idx = df['Prim_Time_ms'].idxmax()
max_kruskal_idx = df['Kruskal_Time_ms'].idxmax()

plt.annotate(f"Prim: {df.loc[max_prim_idx, 'Prim_Time_ms']:.2f}ms",
             xy=(df.loc[max_prim_idx, 'Vertices'], df.loc[max_prim_idx, 'Prim_Time_ms']),
             xytext=(10, 20), textcoords='offset points',
             bbox=dict(boxstyle='round,pad=0.5', fc='#2E86AB', alpha=0.7),
             arrowprops=dict(arrowstyle='->', connectionstyle='arc3,rad=0', color='#2E86AB'),
             fontsize=10, color='white', fontweight='bold')

plt.annotate(f"Kruskal: {df.loc[max_kruskal_idx, 'Kruskal_Time_ms']:.2f}ms",
             xy=(df.loc[max_kruskal_idx, 'Vertices'], df.loc[max_kruskal_idx, 'Kruskal_Time_ms']),
             xytext=(10, -30), textcoords='offset points',
             bbox=dict(boxstyle='round,pad=0.5', fc='#A23B72', alpha=0.7),
             arrowprops=dict(arrowstyle='->', connectionstyle='arc3,rad=0', color='#A23B72'),
             fontsize=10, color='white', fontweight='bold')

plt.tight_layout()
plt.savefig('charts/execution_time_comparison.png', dpi=300, bbox_inches='tight')
print("✓ Saved: charts/execution_time_comparison.png")
plt.close()

# Chart 2: Operation Count Comparison
print("\nGenerating Chart 2: Operation Count vs Graph Size...")
plt.figure(figsize=(14, 7))

plt.plot(df['Vertices'], df['Prim_Operations']/1000, 
         marker='s', markersize=8, linewidth=2.5, 
         label="Prim's Algorithm", color='#2E86AB', alpha=0.9)

plt.plot(df['Vertices'], df['Kruskal_Operations']/1000, 
         marker='o', markersize=8, linewidth=2.5, 
         label="Kruskal's Algorithm", color='#A23B72', alpha=0.9)

plt.xlabel('Number of Vertices', fontsize=13, fontweight='bold')
plt.ylabel('Operation Count (× 1,000)', fontsize=13, fontweight='bold')
plt.title('MST Algorithm Performance: Operation Count vs Graph Size', 
          fontsize=15, fontweight='bold', pad=20)
plt.legend(fontsize=12, loc='upper left', framealpha=0.95)
plt.grid(True, alpha=0.3, linestyle='--')

plt.tight_layout()
plt.savefig('charts/operation_count_comparison.png', dpi=300, bbox_inches='tight')
print("✓ Saved: charts/operation_count_comparison.png")
plt.close()

# Chart 3: Side-by-side Bar Chart for Time (Small, Medium, Large, Extra Large)
print("\nGenerating Chart 3: Time Comparison by Graph Category...")

# Categorize graphs
def categorize(vertices):
    if vertices <= 30:
        return 'Small\n(30 nodes)'
    elif vertices <= 300:
        return 'Medium\n(300 nodes)'
    elif vertices <= 1000:
        return 'Large\n(1000 nodes)'
    else:
        return 'Extra Large\n(1400-1800 nodes)'

df['Category'] = df['Vertices'].apply(categorize)

# Calculate average times per category
avg_times = df.groupby('Category')[['Prim_Time_ms', 'Kruskal_Time_ms']].mean()

categories = ['Small\n(30 nodes)', 'Medium\n(300 nodes)', 'Large\n(1000 nodes)', 'Extra Large\n(1400-1800 nodes)']
avg_times = avg_times.reindex(categories)

fig, ax = plt.subplots(figsize=(12, 7))

x = range(len(categories))
width = 0.35

bars1 = ax.bar([i - width/2 for i in x], avg_times['Prim_Time_ms'], 
               width, label="Prim's Algorithm", color='#2E86AB', alpha=0.9, edgecolor='black', linewidth=1.2)
bars2 = ax.bar([i + width/2 for i in x], avg_times['Kruskal_Time_ms'], 
               width, label="Kruskal's Algorithm", color='#A23B72', alpha=0.9, edgecolor='black', linewidth=1.2)

# Add value labels on bars
for bars in [bars1, bars2]:
    for bar in bars:
        height = bar.get_height()
        ax.text(bar.get_x() + bar.get_width()/2., height,
                f'{height:.2f}ms',
                ha='center', va='bottom', fontsize=10, fontweight='bold')

ax.set_xlabel('Graph Category', fontsize=13, fontweight='bold')
ax.set_ylabel('Average Execution Time (ms)', fontsize=13, fontweight='bold')
ax.set_title('Average Execution Time by Graph Size Category', 
             fontsize=15, fontweight='bold', pad=20)
ax.set_xticks(x)
ax.set_xticklabels(categories, fontsize=11)
ax.legend(fontsize=12, framealpha=0.95)
ax.grid(True, alpha=0.3, axis='y', linestyle='--')

plt.tight_layout()
plt.savefig('charts/time_by_category.png', dpi=300, bbox_inches='tight')
print("✓ Saved: charts/time_by_category.png")
plt.close()

# Chart 4: Performance Difference (Kruskal - Prim)
print("\nGenerating Chart 4: Performance Advantage...")
plt.figure(figsize=(14, 7))

time_diff = df['Kruskal_Time_ms'] - df['Prim_Time_ms']
colors = ['#27AE60' if x < 0 else '#E74C3C' for x in time_diff]

bars = plt.bar(df['Vertices'], time_diff, color=colors, alpha=0.7, edgecolor='black', linewidth=1)

plt.axhline(y=0, color='black', linestyle='-', linewidth=2)
plt.xlabel('Number of Vertices', fontsize=13, fontweight='bold')
plt.ylabel('Time Difference: Kruskal - Prim (ms)', fontsize=13, fontweight='bold')
plt.title("Performance Advantage: Negative = Prim Faster, Positive = Kruskal Faster", 
          fontsize=15, fontweight='bold', pad=20)
plt.grid(True, alpha=0.3, axis='y', linestyle='--')

# Add legend
from matplotlib.patches import Patch
legend_elements = [
    Patch(facecolor='#27AE60', alpha=0.7, label='Prim Faster'),
    Patch(facecolor='#E74C3C', alpha=0.7, label='Kruskal Faster')
]
plt.legend(handles=legend_elements, fontsize=12, framealpha=0.95)

plt.tight_layout()
plt.savefig('charts/performance_advantage.png', dpi=300, bbox_inches='tight')
print("✓ Saved: charts/performance_advantage.png")
plt.close()

# Chart 5: Scatter Plot - Time vs Edges
print("\nGenerating Chart 5: Execution Time vs Number of Edges...")
fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(16, 6))

# Prim
ax1.scatter(df['Edges'], df['Prim_Time_ms'], 
           s=100, c='#2E86AB', alpha=0.6, edgecolors='black', linewidth=1)
ax1.set_xlabel('Number of Edges', fontsize=12, fontweight='bold')
ax1.set_ylabel('Execution Time (ms)', fontsize=12, fontweight='bold')
ax1.set_title("Prim's Algorithm: Time vs Edges", fontsize=14, fontweight='bold')
ax1.grid(True, alpha=0.3, linestyle='--')

# Kruskal
ax2.scatter(df['Edges'], df['Kruskal_Time_ms'], 
           s=100, c='#A23B72', alpha=0.6, edgecolors='black', linewidth=1)
ax2.set_xlabel('Number of Edges', fontsize=12, fontweight='bold')
ax2.set_ylabel('Execution Time (ms)', fontsize=12, fontweight='bold')
ax2.set_title("Kruskal's Algorithm: Time vs Edges", fontsize=14, fontweight='bold')
ax2.grid(True, alpha=0.3, linestyle='--')

plt.tight_layout()
plt.savefig('charts/time_vs_edges.png', dpi=300, bbox_inches='tight')
print("✓ Saved: charts/time_vs_edges.png")
plt.close()

# Chart 6: Cost Verification (should be identical)
print("\nGenerating Chart 6: MST Cost Verification...")
plt.figure(figsize=(14, 7))

plt.plot(df['Vertices'], df['Prim_Cost'], 
         marker='s', markersize=8, linewidth=2.5, 
         label="Prim's MST Cost", color='#2E86AB', alpha=0.9)

plt.plot(df['Vertices'], df['Kruskal_Cost'], 
         marker='o', markersize=6, linewidth=2, linestyle='--',
         label="Kruskal's MST Cost", color='#A23B72', alpha=0.7)

plt.xlabel('Number of Vertices', fontsize=13, fontweight='bold')
plt.ylabel('Total MST Cost', fontsize=13, fontweight='bold')
plt.title('MST Cost Verification: Both Algorithms Produce Identical Results', 
          fontsize=15, fontweight='bold', pad=20)
plt.legend(fontsize=12, loc='upper left', framealpha=0.95)
plt.grid(True, alpha=0.3, linestyle='--')

# Add verification text
all_match = (df['Cost_Match'] == 'YES').all()
color = '#27AE60' if all_match else '#E74C3C'
status = '✓ All Costs Match' if all_match else '✗ Cost Mismatch Found'

plt.text(0.5, 0.95, status, 
         transform=plt.gca().transAxes,
         fontsize=14, fontweight='bold', color=color,
         ha='center', va='top',
         bbox=dict(boxstyle='round,pad=0.5', facecolor='white', alpha=0.8, edgecolor=color, linewidth=2))

plt.tight_layout()
plt.savefig('charts/cost_verification.png', dpi=300, bbox_inches='tight')
print("✓ Saved: charts/cost_verification.png")
plt.close()

print("\n" + "="*70)
print("✓ All charts generated successfully!")
print("="*70)
print("\nGenerated files in 'charts/' directory:")
print("  1. execution_time_comparison.png")
print("  2. operation_count_comparison.png")
print("  3. time_by_category.png")
print("  4. performance_advantage.png")
print("  5. time_vs_edges.png")
print("  6. cost_verification.png")
print("\nYou can now insert these images into your README.md!")
print("\nExample markdown:")
print("  ![Execution Time](charts/execution_time_comparison.png)")

